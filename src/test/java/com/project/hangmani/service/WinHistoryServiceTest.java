package com.project.hangmani.service;

import com.project.hangmani.model.winhistory.WinHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class WinHistoryServiceTest {
    @Autowired
    private WinHistoryService winHistoryService;
    @Autowired
    private WinHistoryRepository winHistoryRepository;

    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;
        TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        WinHistoryRepository winHistoryRepository() {
            return new WinHistoryRepository(dataSource);
        }

        @Bean
        WinHistoryService winHistoryService() {
            return new WinHistoryService(winHistoryRepository());
        }
    }
    @Test
    @DisplayName("DB 접속 및 데이터 확인")
    void DBConnectTest(){
//        winHistoryService
    }
}