package com.project.hangmani.repository;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreFilterDTO;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class StoreRepositoryTest {
    private StoreRepository storeRepository;
    private DataSource dataSource;
    private Util utilMock;
    private JdbcTemplate template;
    private String testStoreUuid= "8c354eaa-ac2c-11ed-9b15-12ebd169e012";
    private String failStoreUuid = "c31ad72-ac2c-11ed-9b15-12ebd169e012";
    @BeforeEach
    void TestConfig() {
        DatabaseInit dbInit = new DatabaseInit();
        dataSource = dbInit.loadDataSource("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_LOWER=TRUE", "sa", "");
        template = dbInit.loadJdbcTemplate(dataSource);
        dbInit.loadScript(template);

        utilMock = new Util();//Mockito.mock(Util.class);
        storeRepository = new StoreRepository(dataSource, utilMock);
    }
    @AfterEach
    void afterDB() {

    }
    @Test
    void getStoreInfoByLatitudeLongitude() {
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
        Assertions.assertThat(tableList.size()).isPositive();

    }
    @Test
    void getStoreInfoByUuid() {
        //given
        //when
        List<Store> stores = storeRepository.getStoreInfoByUuid(testStoreUuid);
        //then
        Assertions.assertThat(testStoreUuid).isEqualTo(stores.get(0).getStoreUuid());
    }
    @Test
    void notExistStoreInfoByUuid() {
        List<Store> stores = storeRepository.getStoreInfoByUuid(failStoreUuid);
        Assertions.assertThat(stores.size()).isZero();
    }

    @Test
    void getStoreInfoWithWinCountBySidoSigugun() {
        //given
        RequestStoreFilterDTO requestDTO = RequestStoreFilterDTO.builder()
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
        List<Store> storeInfoTest = storeRepository.getStoreInfoWithWinRankBySidoSigugun(requestDTO);
//        log.info("result: {}", storeInfoTest);
        //then
       Assertions.assertThat(storeInfoTest.size()).isPositive();
    }

    @Test
    void wrongSidoStoreInfoWithWinCount() {
        //given
        RequestStoreFilterDTO requestDTO = RequestStoreFilterDTO.builder()
                .sido("인천광역시")
                .sigugun("부평구")
                .filter("1st")
                .lottoID(1)
                .offset(0)
                .limit(10)
                .userLatitude(37.39130205)
                .userLongitude(127.077625)
                .build();
        //when
        List<Store> storeInfoTest = storeRepository.getStoreInfoWithWinRankBySidoSigugun(requestDTO);
        //then
        Assertions.assertThat(storeInfoTest.size()).isZero();
    }

    /**
     * repository에서는 limit,offset관련된 작업을 하지 않기 때문에 영향을 받지 않아야한다.
     */
    @Test
    void unNormalOffsetStoreInfoWinRankBySidoSigugun() {
        int offset = 5;
        int limit = 1;
        //given
        RequestStoreFilterDTO requestDTO = RequestStoreFilterDTO.builder()
                .sido("인천")
                .sigugun("부평구")
                .filter("1st")
                .lottoID(1)
                .offset(offset)
                .limit(limit)
                .userLatitude(37.39130205)
                .userLongitude(127.077625)
                .build();
        //when
        List<Store> storeInfoTest = storeRepository.getStoreInfoWithWinRankBySidoSigugun(requestDTO);

        //then
        Assertions.assertThat(storeInfoTest.size()).isNotZero();
    }

    @Test
    void successStoreInfoWithWinCountByLatitudeLongitude() {
        //given
        RequestStoreFilterDTO requestDTO = RequestStoreFilterDTO.builder()//인천 부평구
                .userLatitude(37.39130205)
                .userLongitude(127.077625)
                .lottoID(1)
                .startLatitude(37.4357)
                .endLatitude(37.5184)
                .startLongitude(126.6779)
                .endLongitude(126.7735)
                .build();
        //when
        List<Store> result = storeRepository.getStoreInfoWithWinRankByLatitudeLongitude(requestDTO);

        //then
        Assertions.assertThat(result.size()).isPositive();
    }

    @Test
    void updateStoreInfo() {
    }

    @Test
    void getStoreByNameLatiLongi() {
    }

    @Test
    void deleteByStoreUuid() {
    }

    @Test
    void deleteByLatitudeLongitude() {
    }

    @Test
    @Transactional
    void successInsertStoreInfo() {
        //given
        StoreDTO.RequestStoreInsertDTO requestDTO = StoreDTO.RequestStoreInsertDTO.builder()
                .storeName("테스트 상점이름")
                .storeAddress("서울 강남구 테스트주소")
                .storeLatitude(37.4921)
                .storeLongitude(127.0443)
                .build();

        //when
        String storeUuid = storeRepository.insertStoreInfo(requestDTO);
        List<Store> stores = storeRepository.getStoreInfoByUuid(storeUuid);
        //then
        Assertions.assertThat(storeUuid).isEqualTo(stores.get(0).getStoreUuid());
    }
    @Test
    void failInsertStoreInfo() {
        //given
        StoreDTO.RequestStoreInsertDTO requestDTO = StoreDTO.RequestStoreInsertDTO.builder()
                .storeName("테스트 상점이름")
                .storeAddress("")//올바르지 않은 주소
                .storeLatitude(37.4921)
                .storeLongitude(127.0443)
                .build();
        //when
        String storeUuid = storeRepository.insertStoreInfo(requestDTO);
        //then
        Assertions.assertThat(storeUuid).isNull();
    }
}