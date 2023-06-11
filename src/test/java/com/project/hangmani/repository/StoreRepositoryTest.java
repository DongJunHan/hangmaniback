package com.project.hangmani.repository;

import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO.RequestStoreFilterDTO;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

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
        utilMock = Mockito.mock(Util.class);
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
                log.info("@@: {}", a);
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
    void failStoreInfoByUuid() {
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
    void getStoreInfoWithWinCountByLatitudeLongitude() {
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
    void insertStoreInfo() {
    }
}