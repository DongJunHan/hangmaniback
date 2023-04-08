package com.project.hangmani.repository;

import com.project.hangmani.domain.Store;
import com.project.hangmani.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Slf4j
class StoreRepositoryTest {
    @Autowired
    StoreRepository storeRepository;


    @Test
    void TestGetStoreInfo() {
        String uuid = "8c352fba-ac2c-11ed-9b15-12ebd169e012";
        Store store = storeRepository.getStoreInfoByUuid(uuid);
        log.info("store={}", store);
    }
}