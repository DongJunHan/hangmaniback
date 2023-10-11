package com.project.hangmani.store.controller;

import com.project.hangmani.common.Common;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "/application-test.properties"
})
@SpringBootTest
@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
public class StoreControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final Common common = new Common();
    private final Double userLatitude = 37.3914817;
    private final Double userLongitude = 127.0777273;
    private final String existUuid = "8c354eaa-ac2c-11ed-9b15-12ebd169e012";

    @Test
    @DisplayName("시도/시구군 당첨내역 상점 정보 성공")
    void getWithWinHistoryByAreaSuccess() throws Exception {
        //given
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("filter", "1st");
        paramMap.put("userLatitude", userLatitude);
        paramMap.put("userLongitude", userLongitude);
        paramMap.put("sido", "인천");
        paramMap.put("sigugun", "부평구");
        paramMap.put("lottoID", 1);
        paramMap.put("startLatitude", null);
        paramMap.put("endLatitude", null);
        paramMap.put("startLongitude", null);
        paramMap.put("endLongitude", null);
        paramMap.put("limit", null);
        paramMap.put("offset", null);
        String json = common.convertMapToJson(paramMap);
        System.out.println("json = " + json);
        //when then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn();
    }

    @Test
    @DisplayName("없는 시도/시구군 당첨내역 상점 정보")
    void getWithWinHistoryByAreaFail() throws Exception {
        //given
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("filter", "1st");
        paramMap.put("userLatitude", userLatitude);
        paramMap.put("userLongitude", userLongitude);
        paramMap.put("sido", "인천인천시");
        paramMap.put("sigugun", "부평구");
        paramMap.put("lottoID", 1);
        paramMap.put("startLatitude", null);
        paramMap.put("endLatitude", null);
        paramMap.put("startLongitude", null);
        paramMap.put("endLongitude", null);
        paramMap.put("limit", null);
        paramMap.put("offset", null);
        String json = common.convertMapToJson(paramMap);
        //when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("위도/경도 밤위로 상점 데이터 확인")
    void getWithWinHistoryByCoordinatesSuccess() throws Exception {
        //given
        Double startLatitude = 37.469443;
        Double endLatitude = 37.523216;
        Double startLongitude = 126.688536;
        Double endLongitude = 126.746722;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("filter", "1st");
        paramMap.put("userLatitude", userLatitude);
        paramMap.put("userLongitude", userLatitude);
        paramMap.put("sido", null);
        paramMap.put("sigugun", null);
        paramMap.put("lottoID", 1);
        paramMap.put("startLatitude", startLatitude);
        paramMap.put("endLatitude", endLatitude);
        paramMap.put("startLongitude", startLongitude);
        paramMap.put("endLongitude", endLongitude);
        paramMap.put("limit", null);
        paramMap.put("offset", null);

        String json = common.convertMapToJson(paramMap);
        //when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());

    }

    @Test
    @DisplayName("없는 위도/경도로 당첨내역 상정정보 요청")
    void getWithWinHistoryByCoordinatesFail() throws Exception {
        //given
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("filter", "1st");
        paramMap.put("userLatitude", userLatitude);
        paramMap.put("userLongitude", userLatitude);
        paramMap.put("sido", null);
        paramMap.put("sigugun", null);
        paramMap.put("lottoID", 1);
        paramMap.put("startLatitude", 0.1);
        paramMap.put("endLatitude", 1.1);
        paramMap.put("startLongitude", 0.0);
        paramMap.put("endLongitude", 1.1);
        paramMap.put("limit", null);
        paramMap.put("offset", null);
        String json = common.convertMapToJson(paramMap);
        //when , then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/store")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("UUID를 가지고 하나의 상점 데이터 가져온다")
    void storeInfoTest() throws Exception {
        //when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/store/{storeUuid}", existUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.storeUuid").value(existUuid));
    }

    @Test
    @DisplayName("없는 UUID 상점 데이터 확인 시도")
    void failStoreInfoTest() throws Exception {
        //when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/store/{storeUuid}", "8c354eaa-ac2c-11ed-9b15-12ebd169e011"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("상점정보를 찾을 수 없습니다."))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @SneakyThrows
    @Test
    @DisplayName("동일한 상점정보 삽입")
    void storeInfoInsertFail() {
        //given
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("storeAddress", "인천 부평구 신촌로7번길 3 1층");
        paramMap.put("storeLatitude", 37.484812);
        paramMap.put("storeLongitude", 126.708757);
        paramMap.put("storeName", "찬스센타백운점");
        paramMap.put("storeBizNo", null);
        paramMap.put("storeTelNum", null);
        paramMap.put("storeMobileNum", null);
        paramMap.put("storeOpenTime", null);
        paramMap.put("storeCloseTime", null);
        paramMap.put("filesData", null);

        String json = common.convertMapToJson(paramMap);
        //when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/store")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("이미 존재하는 상점이 있습니다."))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @SneakyThrows
    @Test
    @DisplayName("상정 정보 삽입 성공")
    void storeInsertSuccess() {
        //given
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("storeAddress", "인천 부평구 테스트로 2 1층");
        paramMap.put("storeLatitude", 37.504812);
        paramMap.put("storeLongitude", 126.80757);
        paramMap.put("storeName", "test store");
        paramMap.put("storeBizNo", null);
        paramMap.put("storeTelNum", null);
        paramMap.put("storeMobileNum", null);
        paramMap.put("storeOpenTime", null);
        paramMap.put("storeCloseTime", null);
        paramMap.put("filesData", null);

        String json = common.convertMapToJson(paramMap);
        //when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/store")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("CREATED"))
                .andExpect(jsonPath("$.data.storeName").value("test store"));
    }

    @SneakyThrows
    @Test
    @DisplayName("상정 정보 업데이트 성공")
    void updateStoreSuccess() {
        //given
        final String changeStoreName = "찬스센타백운점 업데이트";
        final String changeStoreBizNo = "1220983101";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("storeAddress", "인천 부평구 신촌로7번길 3 1층");
        paramMap.put("storeLatitude", 37.484812);
        paramMap.put("storeLongitude", 126.708757);
        paramMap.put("storeName", changeStoreName);
        paramMap.put("storeBizNo", changeStoreBizNo);
        paramMap.put("storeTelNum", "032-523-4188");
        paramMap.put("storeMobileNum", "01033138977");
        paramMap.put("storeOpenTime", LocalDateTime.now());
        paramMap.put("storeCloseTime", LocalDateTime.now());
        paramMap.put("storeIsActivity", true);
        paramMap.put("storeSido", "인천");
        paramMap.put("storeSigugun", "부평구");
        String json = common.convertMapToJson(paramMap);
        //when, then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/store/{storeUuid}", existUuid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.storeBizNo").value(changeStoreBizNo))
                .andExpect(jsonPath("$.data.storeName").value(changeStoreName));
    }

    @SneakyThrows
    @Test
    @DisplayName("없는 상점 업데이트")
    void NotExistUpdateStore() {
        final String changeStoreName = "찬스센타백운점 업데이트";
        final String changeStoreBizNo = "1220983101";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("storeAddress", "인천 부평구 신촌로7번길 3 1층");
        paramMap.put("storeLatitude", 37.484812);
        paramMap.put("storeLongitude", 126.708757);
        paramMap.put("storeName", changeStoreName);
        paramMap.put("storeBizNo", changeStoreBizNo);
        paramMap.put("storeTelNum", "032-523-4188");
        paramMap.put("storeMobileNum", "01033138977");
        paramMap.put("storeOpenTime", LocalDateTime.now());
        paramMap.put("storeCloseTime", LocalDateTime.now());
        String json = common.convertMapToJson(paramMap);
        //when, then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/store/{storeUuid}", "8c354eaa-ac2c-11ed-9b15-12ebd169e011")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("상점정보를 찾을 수 없습니다."));
    }
}
