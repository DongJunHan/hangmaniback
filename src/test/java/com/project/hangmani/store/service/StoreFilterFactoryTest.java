package com.project.hangmani.store.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "/application-test.properties"
})
@SpringBootTest
class StoreFilterFactoryTest {
    @Autowired
    StoreFilterFactory storeFilterFactory;

    @Test
    @DisplayName("store factory bean test")
    void createStoreFilterFactory() {
        StoreFilter storeFilterInstance = storeFilterFactory.getInstance("coordinates");
        Assertions.assertThat(storeFilterInstance).isInstanceOf(StoreFilterCoordinates.class);
    }
}