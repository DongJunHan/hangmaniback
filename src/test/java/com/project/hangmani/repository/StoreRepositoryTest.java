package com.project.hangmani.repository;

import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreFilterDTO;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class StoreRepositoryTest {
    private static StoreRepository storeRepository;
    private static DataSource dataSource;
    private static Util utilMock;
    private static JdbcTemplate template;
    private String testStoreUuid= "8c31ad72-ac2c-11ed-9b15-12ebd169e012";
    private String failStoreUuid = "c31ad72-ac2c-11ed-9b15-12ebd169e012";
    @BeforeAll
    static void preDB() {
        dataSource = new DriverManagerDataSource("jdbc:h2:mem:test","sa","");
        utilMock = new Util();//Mockito.mock(Util.class);
        storeRepository = new StoreRepository(dataSource, utilMock);

        template = new JdbcTemplate(dataSource);
        Resource schema = new ClassPathResource("schema.sql");
        Resource data = new ClassPathResource("data.sql");
        try {
            ScriptUtils.executeSqlScript(template.getDataSource().getConnection(), schema);
            ScriptUtils.executeSqlScript(template.getDataSource().getConnection(), data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String packageName = Util.class.getPackage().getName();
        String className = Util.class.getSimpleName();
        String qualifiedClassName = packageName + "." + className;

        // 사용자 정의 함수 등록
        template.execute("CREATE ALIAS IF NOT EXISTS get_distance FOR \"" + qualifiedClassName + ".getDistance\";");
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
                log.info("value: {}", a);
            }
        }
        Assertions.assertThat(tableList.size()).isPositive();

    }
    @Test
    void getStoreInfoByUuid() {
        //given

        //when
        Store store = storeRepository.getStoreInfoByUuid(testStoreUuid);
        //then
        Assertions.assertThat(testStoreUuid).isEqualTo(store.getStoreUuid());
    }
    @Test
    void notExistStoreInfoByUuid() {
        assertThrows(EmptyResultDataAccessException.class, () -> storeRepository.getStoreInfoByUuid(failStoreUuid));
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
        List<Store> result = storeRepository.getStoreInfoWithWinCountBySidoSigugun(requestDTO);
        log.info("result: {}", result);
        //then
        Assertions.assertThat(result.size()).isPositive();
    }

    @Test
    void notExistSidoStoreInfoWinCountBySidoSigugun() {
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
        List<Store> result = storeRepository.getStoreInfoWithWinCountBySidoSigugun(requestDTO);
        log.info("result: {}", result);
        //then
        Assertions.assertThat(result.size()).isZero();
    }
    @Test
    void unNormalOffsetStoreInfoWinCountBySidoSigugun() {
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
        List<Store> result = storeRepository.getStoreInfoWithWinCountBySidoSigugun(requestDTO);
        log.info("result: {}", result);
        //then
        Assertions.assertThat(result.size()).isNotZero();
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
        List<Store> result = storeRepository.getStoreInfoWithWinCountByLatitudeLongitude(requestDTO);

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
        log.info("uuid={}", storeUuid);
        Store store = storeRepository.getStoreInfoByUuid(storeUuid);
        log.info("store={}", store);
        //then
        Assertions.assertThat(storeUuid).isNotNull();
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