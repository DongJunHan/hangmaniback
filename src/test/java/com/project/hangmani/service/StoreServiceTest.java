package com.project.hangmani.service;

import com.project.hangmani.model.store.Store;
import com.project.hangmani.model.store.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
class StoreServiceTest {
    private final String sido = "인천";
    private final String sigugun = "부평구";
    @Autowired
    private StoreService storeService;

    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;
        TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        StoreRepository storeRepository() {
            return new StoreRepository(dataSource);
        }
        @Bean
        StoreService StoreServiceV1() {
            return new StoreService(storeRepository());
        }

    }
    @Test
    @DisplayName("H2 데이터베이스 상점 데이터 확인")
    void StoreInfoTest(){
        List<Store> data = storeService.getStoreInfo(sido, sigugun);
        log.info("result={}",data);
        Assertions.assertThat(data.size()).isEqualTo(171);
    }
}