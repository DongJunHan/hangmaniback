package com.project.hangmani.service;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.domain.WinHistory;
import com.project.hangmani.repository.WinHistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.List;
class WinHistoryServiceTest {
    private final String sido = "인천";
    private final String sigugun = "부평구";
    private WinHistoryService winHistoryService;
    private static DataSource dataSource;
    private static JdbcTemplate template;
    private static DatabaseInit dbInit;
    @BeforeAll
    static void initOnce() {
        dbInit = new DatabaseInit();
        dataSource = dbInit.loadDataSource();
        template = dbInit.loadJdbcTemplate(dataSource);
    }
    @BeforeEach
    void TestConfig() {
        dbInit.loadScript(template);
        WinHistoryRepository winHistoryRepository = new WinHistoryRepository(dataSource);
        winHistoryService = new WinHistoryService(winHistoryRepository);
    }
    @Test
    @DisplayName("H2 데이터베이스 당첨내역 데이터 확인")
    void WinHistoryListTest(){
        List<WinHistory> data = winHistoryService.getData(sido, sigugun);
        Assertions.assertThat(data.size()).isPositive();
    }
}