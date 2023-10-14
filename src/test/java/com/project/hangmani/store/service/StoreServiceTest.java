package com.project.hangmani.store.service;

import com.project.hangmani.exception.AlreadyExistStore;
import com.project.hangmani.exception.NotFoundStore;
import com.project.hangmani.store.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "classpath:/application-test.properties"
})
@SpringBootTest
@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StoreServiceTest {
    @Autowired
    private StoreService storeService;
    private final String existUuid = "8c354eaa-ac2c-11ed-9b15-12ebd169e012";

    @Test
    @DisplayName("시도/시구군 당첨내역 상점 정보 성공")
    void getWithWinHistoryByAreaSuccess() {
        //given
        Double userLatitude = 37.3914817;
        Double userLongitude = 127.0777273;

        //when
        List<ResponseFilterDTO> storeInfo =
                storeService.getStoreInfo(RequestFilterDTO.builder()
                        .filter("1st")
                        .userLatitude(userLatitude)
                        .userLongitude(userLongitude)
                        .sido("인천")
                        .sigugun("부평구")
                        .lottoID(1)
                        .build());
        //then
        assertThat(storeInfo.size()).isPositive();

    }

    @Test
    @DisplayName("없는 시도/시구군 당첨내역 상점 정보")
    void getWithWinHistoryByAreaFail() {
        //given
        Double userLatitude = 37.3914817;
        Double userLongitude = 127.0777273;
        //when
        List<ResponseFilterDTO> storeInfo =
                storeService.getStoreInfo(RequestFilterDTO.builder()
                        .filter("1st")
                        .userLatitude(userLatitude)
                        .userLongitude(userLongitude)
                        .sido("인천공욕시")
                        .sigugun("부평구")
                        .lottoID(1)
                        .build());
        //then
        assertThat(storeInfo.size()).isZero();
    }

    @Test
    @DisplayName("위도/경도 밤위로 상점 데이터 확인")
    void getWithWinHistoryByCoordinatesSuccess() {
        //given
        Double startLatitude = 37.469443;
        Double endLatitude = 37.523216;
        Double startLongitude = 126.688536;
        Double endLongitude = 126.746722;

        Double userLatitude = 37.3914817;
        Double userLongitude = 127.0777273;

        RequestFilterDTO requestStoreDTO = RequestFilterDTO.builder()
                .startLatitude(startLatitude)
                .endLatitude(endLatitude)
                .startLongitude(startLongitude)
                .endLongitude(endLongitude)
                .limit(20)
                .userLatitude(userLatitude)
                .userLongitude(userLongitude)
                .lottoID(1)
                .build();
        //when
        List<ResponseFilterDTO> storeInfoList = storeService.getStoreInfo(requestStoreDTO);
        //then
        assertThat(storeInfoList.size()).isPositive();
    }

    @Test
    @DisplayName("없는 위도/경도로 당첨내역 상정정보 요청")
    void getWithWinHistoryByCoordinatesFail() {
        //given
        RequestFilterDTO requestDTO = RequestFilterDTO.builder()//인천 부평구
                .userLatitude(37.39130205)
                .userLongitude(127.077625)
                .lottoID(1)
                .startLatitude(0.0)
                .endLatitude(1.1)
                .lottoID(1)
                .startLongitude(0.0)
                .endLongitude(1.1)
                .build();
        //when
        List<ResponseFilterDTO> result = storeService.getStoreInfo(requestDTO);

        //then
        assertThat(result.size()).isZero();
    }

    @Test
    @DisplayName("UUID를 가지고 하나의 상점 데이터 확인")
    void storeInfoTest() {
        //given
        String storeUuid = "8c354eaa-ac2c-11ed-9b15-12ebd169e012";
//        String storeUuid = "4d128918-ac2c-11ed-9b15-12ebd169e012";
        //when
        ResponseDTO storeInfo = storeService.getStoreInfo(storeUuid);
        //then
        assertThat(storeInfo).isInstanceOf(ResponseDTO.class);
    }

    @Test
    @DisplayName("잘못된 UUID로 에러 확인")
    void failStoreInfoTest() {
        //given
        String storeUuid = "4d128918-ac2c-11ed-9b15-12ebd169e011";
        //when
        assertThrows(NotFoundStore.class, () -> storeService.getStoreInfo(storeUuid));


    }

    @Test
    @DisplayName("동일한 상점정보 삽입")
    void storeInfoInsertFail() {
        RequestInsertDTO request = RequestInsertDTO.builder()
                .storeAddress("인천 부평구 신촌로7번길 3 1층")
                .storeLatitude(37.484812)
                .storeLongitude(126.708757)
                .storeName("찬스센타백운점")
                .build();
        assertThatThrownBy(() -> storeService.insert(request))
                .isInstanceOf(AlreadyExistStore.class);
    }

    @Test
    @DisplayName("상점 삭제 성공")
    void deleteStoreSuccess() {
        //given
        RequestDeleteDTO request = RequestDeleteDTO.builder().storeUuid(existUuid).build();
        //when
        int ret = storeService.delete(request);
        //then
        assertThat(ret).isEqualTo(1);
    }
    @Test
    @DisplayName("없는 상점 삭제")
    void notExistDeleteStore() {
        //given
        RequestDeleteDTO request = RequestDeleteDTO.builder().storeUuid("not-exist").build();
        //when, then
        assertThatThrownBy(() -> storeService.delete(request)).isInstanceOf(NotFoundStore.class);
    }

    @Test
    @DisplayName("주소에서 시/도, 시/구/군 파싱하는 테스트")
    void addressParsing() {
        //ex. 서울 강남구(서울 강남구 테헤란로63길 12 LG선릉에클라트 B동 107호),
        // 경기 성남시(경기 성남시 분당구 양현로94번길 21 구두28호점), 경남 창원시 마산합포구(경남 창원시 마산합포구 해안대로 58 1층)

        //1. 시/도를 파싱 '특별시'는 뺀 예외 - 세종
        //2. 시/구/군에서 시로 시작하면 뒤에 구가 있는지 확인.
        //3. 구가 있으면 구까지 파싱 아니면 그 앞까지만 시/구/군으로 파싱
        Map<String, String> sidoMap = new HashMap<>();
        String[] ret = new String[2];
        sidoMap.put("경기도", "경기");
        sidoMap.put("서울특별시", "서울");
        sidoMap.put("세종특별자치시", "세종");
        sidoMap.put("제주특별자치도", "제주");
        sidoMap.put("제주도", "제주");
        String address = "제주특별자치도 서귀포시 일주동로 8665(서귀동)";
//        String address = "세종특별자치시 정안세종로 1567(산울동)";
//        String address = "경기도 성남시 분당구 양현로94번길 21 구두28호점";
        String[] splitAddress = address.split(" ");
        String convertSido = sidoMap.get(splitAddress[0]);
        if (convertSido != null) {
            splitAddress[0] = convertSido;
            if (splitAddress[0].equals("세종")) {
                ret[0] = splitAddress[0];
                ret[1] = null;
            }
        }

        ret[0] = splitAddress[0];
        int firstLength = splitAddress[1].length();
        int secondLength = splitAddress[2].length();
        if (splitAddress[1].substring(firstLength - 1, firstLength).equals("시") &&
                splitAddress[2].substring(secondLength - 1, secondLength).equals("구"))
            ret[1] = splitAddress[1] + " " + splitAddress[2];
        else
            ret[1] = splitAddress[1];
    }

}