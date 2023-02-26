package com.project.hangmani.service;

import com.project.hangmani.model.winhistory.WinHistory;
import com.project.hangmani.model.winhistory.WinHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class WinHistoryServiceTest {
    private final String sido = "인천";
    private final String sigugun = "부평구";
    @Autowired
    private WinHistoryService winHistoryService;
//    @Autowired
//    private WinHistoryRepository winHistoryRepository;

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
        log.info("result={}",data);
        Assertions.assertThat(data.size()).isEqualTo(582);
    }
}