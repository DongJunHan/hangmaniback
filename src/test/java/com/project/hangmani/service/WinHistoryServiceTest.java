package com.project.hangmani.service;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.domain.WinHistory;
import com.project.hangmani.repository.WinHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.List;

@Slf4j
class WinHistoryServiceTest {
    private final String sido = "인천";
    private final String sigugun = "부평구";
    private WinHistoryService winHistoryService;
    private DataSource dataSource;
    private JdbcTemplate template;
    @BeforeEach
    void TestConfig() {
        DatabaseInit dbInit = new DatabaseInit();
        dataSource = dbInit.loadDataSource("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_LOWER=TRUE", "sa", "");
        template = dbInit.loadJdbcTemplate(dataSource);
        dbInit.loadScript(template);
        WinHistoryRepository winHistoryRepository = new WinHistoryRepository(dataSource);
        winHistoryService = new WinHistoryService(winHistoryRepository);
    }
    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;
        TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        WinHistoryRepository winHistoryRepository() {
            return new WinHistoryRepository(dataSource);
        }
        @Bean
        WinHistoryService winHistoryServiceV1() {
            return new WinHistoryService(winHistoryRepository());
        }

    }
    @Test
    @DisplayName("H2 데이터베이스 당첨내역 데이터 확인")
    void WinHistoryListTest(){
        List<WinHistory> data = winHistoryService.getData(sido, sigugun);
        Assertions.assertThat(data.size()).isPositive();
    }
}