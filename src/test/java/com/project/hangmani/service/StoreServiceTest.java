package com.project.hangmani.service;

import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreDTO;
import com.project.hangmani.dto.StoreDTO.ResponseStoreDTO;
import com.project.hangmani.repository.StoreRepository;
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
        RequestStoreDTO requestStoreDTO = new RequestStoreDTO(sido, sigugun);
        ResponseStoreDTO data = storeService.getStoreInfo(requestStoreDTO);
        log.info("result={}",data);
        Assertions.assertThat(data.getStoreList().size()).isEqualTo(171);
    }
}