package com.project.hangmani.store.repository;

import com.project.hangmani.store.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "/application-test.properties"
})
@SpringBootTest
@Sql(value = {"/drop.sql","/schema.sql","/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StoreRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;
    private final String testStoreUuid = "8c354eaa-ac2c-11ed-9b15-12ebd169e012";
    private final String failStoreUuid = "c31ad72-ac2c-11ed-9b15-12ebd169e012";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @AfterEach
    public void afterEach() {
        final List<String> truncateQueries = getTruncateQueries(jdbcTemplate);
        truncateTables(jdbcTemplate, truncateQueries);
    }

    private List<String> getTruncateQueries(final JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList("SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String.class);
    }

    private void truncateTables(final JdbcTemplate jdbcTemplate, final List<String> truncateQueries) {
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY FALSE");
        truncateQueries.forEach(v -> execute(jdbcTemplate, v));
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY TRUE");
    }

    private void execute(final JdbcTemplate jdbcTemplate, final String query) {
        jdbcTemplate.execute(query);
    }
    @Test
    void testGetTableLists() {
        List<List<String>> tableList = storeRepository.getTableList();
        for (List<String> t :
                tableList) {
            for (String a :
                    t) {
                System.out.println("DB table = " + a);
//                log.info("value: {}", a);
            }
        }
        assertThat(tableList.size()).isPositive();

    }

    @Test
    @DisplayName("uuid로 상점 데이터 가져오기")
    void getStoreInfoByUuidSuccess() {
        //given
        //when
        List<StoreDTO> stores = storeRepository.getByUuid(testStoreUuid);
        //then
        assertThat(testStoreUuid).isEqualTo(stores.get(0).getStoreUuid());
    }

    @Test
    @DisplayName("없는 uuid로 상점 데이터 가져오기")
    void notExistStoreInfoByUuid() {
        List<StoreDTO> stores = storeRepository.getByUuid(failStoreUuid);
        assertThat(stores.size()).isZero();
    }

    @Test
    @DisplayName("시도/시구군 당첨내역 가져오기 성공")
    void getWithWinHistoryByAreaSuccess() {
        //given
        RequestFilterDTO requestDTO = RequestFilterDTO.builder()
                .sido("인천")
                .sigugun("부평구")
                .filter("1st")
                .lottoID(1)
                .offset(0)
                .limit(10)
                .userLatitude(37.39130205)
                .userLongitude(127.077625)
                .build();

        //when
        List<StoreDTO> storeInfoTest = storeRepository.getWithWinHistoryByArea(requestDTO);
//        log.info("result: {}", storeInfoTest);
        //then
        assertThat(storeInfoTest.size()).isPositive();
    }

    @Test
    @DisplayName("없는 시도/시구군 당첨내역 가져오기")
    void getWithWinHistoryByAreaFail() {
        //given
        RequestFilterDTO requestDTO = RequestFilterDTO.builder()
                .sido("인천관욕시")
                .sigugun("부평구")
                .filter("1st")
                .lottoID(1)
                .offset(0)
                .limit(10)
                .userLatitude(37.39130205)
                .userLongitude(127.077625)
                .build();
        //when
        List<StoreDTO> storeInfoTest = storeRepository.getWithWinHistoryByArea(requestDTO);
        //then
        assertThat(storeInfoTest.size()).isZero();
    }


    @Test
    @DisplayName("위도/경도로 당첨내역 가져오기")
    void getWithWinHistoryByCoordinatesSuccess() {
        //given
        RequestFilterDTO requestDTO = RequestFilterDTO.builder()//인천 부평구
                .startLatitude(37.4357)
                .endLatitude(37.5184)
                .startLongitude(126.6779)
                .endLongitude(126.7735)
                .userLatitude(37.39130205)
                .userLongitude(127.077625)
                .lottoID(1)
                .build();
        //when
        List<StoreDTO> result = storeRepository.getWithWinHistoryByCoordinates(requestDTO);

        //then
        assertThat(result.size()).isPositive();
    }

    @Test
    @DisplayName("없는 위도/경도 당첨내역 가져오기")
    void getWithWinHistoryByCoordinatesFail() {
        //given
        RequestFilterDTO requestDTO = RequestFilterDTO.builder()//인천 부평구
                .userLatitude(37.39130205)
                .userLongitude(127.077625)
                .lottoID(1)
                .startLatitude(0.0)
                .endLatitude(1.1)
                .startLongitude(0.0)
                .endLongitude(1.1)
                .build();
        //when
        List<StoreDTO> result = storeRepository.getWithWinHistoryByCoordinates(requestDTO);

        //then
        assertThat(result.size()).isZero();
    }

    @Test
    @DisplayName("상점 정보 업데이트 성공")
    void updateSuccess() {
        //given
        RequestUpdateDTO request = RequestUpdateDTO.builder()
                .storeName("찬스센타백운점1")
                .storeAddress("인천 부평구 신촌로7번길 3 1층")
                .storeLatitude(37.48481)
                .storeLongitude(126.708757)
                .storeBizNo("1220983100")
                .storeTelNum("032-523-4189")
                .storeMobileNum("01033138977")
                .storeSido("인천")
                .storeSigugun("부평구")
                .build();
        //when
        int updateRet = storeRepository.update(testStoreUuid, request.of());
        //then
        assertThat(updateRet).isEqualTo(1);

    }

    @Test
    @DisplayName("없는 상정 Uuid로 업데이트")
    void updateFailByNotExist() {
        //given
        RequestUpdateDTO request = RequestUpdateDTO.builder()
                .storeName("찬스센타백운점1")
                .storeAddress("인천 부평구 신촌로7번길 3 1층")
                .storeLatitude(37.48481)
                .storeLongitude(126.708757)
                .storeBizNo("1220983100")
                .storeTelNum("032-523-4189")
                .storeMobileNum("01033138977")
                .storeSido("인천")
                .storeSigugun("부평구")
                .build();
        //when
        int updateRet = storeRepository.update(failStoreUuid, request.of());
        //then
        assertThat(updateRet).isEqualTo(0);
    }

    @Test
    @DisplayName("상점 삭제 성공")
    void deleteSuccess() {
        //given
        RequestDeleteDTO request = RequestDeleteDTO.builder()
                .storeUuid(testStoreUuid)
                .build();
        //when
        int delete = storeRepository.delete(request.getStoreUuid());
        //then
        assertThat(delete).isEqualTo(1);
    }

    @Test
    @DisplayName("상점 삭제 실패")
    void deleteFailByNotExist() {
        //given
        RequestDeleteDTO request = RequestDeleteDTO.builder()
                .storeUuid(failStoreUuid)
                .build();
        //when
        int delete = storeRepository.delete(request.getStoreUuid());
        //then
        assertThat(delete).isEqualTo(0);
    }
    @Test
    @DisplayName("상점정보 삽입 성공")
    void addSuccess() {
        //given
        RequestInsertDTO requestDTO = RequestInsertDTO.builder()
                .storeName("테스트 상점이름")
                .storeAddress("서울 강남구 테스트주소")
                .storeLatitude(37.4921)
                .storeLongitude(127.0443)
                .build();

        //when
        String storeUuid = storeRepository.add(requestDTO);
        List<StoreDTO> stores = storeRepository.getByUuid(storeUuid);
        //then
        assertThat(storeUuid).isEqualTo(stores.get(0).getStoreUuid());
    }

    @Test
    @DisplayName("올바르지 않는 주소로 삽입 시도하여 상점 정보 삽입 실패")
    void addFailByNotExistAddress() {
        //given
        RequestInsertDTO requestDTO = RequestInsertDTO.builder()
                .storeName("테스트 상점이름")
                .storeAddress("")
                .storeLatitude(37.4921)
                .storeLongitude(127.0443)
                .build();
        //when
        String storeUuid = storeRepository.add(requestDTO);
        //then
        assertThat(storeUuid).isNull();
    }
}