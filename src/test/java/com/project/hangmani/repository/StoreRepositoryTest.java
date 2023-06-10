package com.project.hangmani.repository;

import com.project.hangmani.domain.Store;
import com.project.hangmani.util.Util;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;

import javax.sql.DataSource;

@TestExecutionListeners(SqlScriptsTestExecutionListener.class)
class StoreRepositoryTest {
    private StoreRepository storeRepository;
    private DataSource dataSource;
    private Util utilMock;
    private String testStoreUuid= "8c31ad72-ac2c-11ed-9b15-12ebd169e012";

    @BeforeEach
    void setDI() {
        dataSource = new DriverManagerDataSource("jdbc:h2:mem:test","sa","");
        utilMock = Mockito.mock(Util.class);
        storeRepository = new StoreRepository(dataSource, utilMock);
    }


    @Test
    void getStoreInfoByLatitudeLongitude() {
    }
    @Test
    void testInMemoryH2DB() {
        TEST test = storeRepository.testInMemoryH2DB(1);
        Assertions.assertThat(1).isEqualTo(test.getNum());
    }
    @Test
    void getStoreInfoByUuid() {
        Store store = storeRepository.getStoreInfoByUuid(testStoreUuid);
        Assertions.assertThat(testStoreUuid).isEqualTo(store.getStoreUuid());
        Assertions.assertThat(store).isInstanceOf(Store.class);
    }

    @Test
    void getStoreInfoWithWinCountBySidoSigugun() {
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

    @Profile("test")
    static class TestCustomFunctionConfig {
        private final JdbcTemplate jdbcTemplate;

        public TestCustomFunctionConfig(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        @BeforeEach
        public void registerFunctions() {
            String packageName = Util.class.getPackage().getName();
            String className = Util.class.getSimpleName();
            String qualifiedClassName = packageName + "." + className;

            // 사용자 정의 함수 등록
            this.jdbcTemplate.execute("CREATE ALIAS IF NOT EXISTS get_distance FOR \"" + qualifiedClassName + ".getDistance\";");
        }
    }
}