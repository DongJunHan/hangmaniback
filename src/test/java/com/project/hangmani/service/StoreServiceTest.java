package com.project.hangmani.service;

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
        Double startLatitude = 37.463;
        Double endLatitude = 37.528;
        Double startLongitude = 127.019;
        Double endLongitude = 127.107;
        RequestStoreDTO requestStoreDTO = new RequestStoreDTO(
                startLatitude,
                endLatitude,
                startLongitude,
                endLongitude,
                129);
        List<ResponseStoreDTO> storeInfoList = storeService.getStoreInfo(requestStoreDTO);
        log.info("result={}",storeInfoList);
        Assertions.assertThat(storeInfoList.size()).isEqualTo(129);
    }
}