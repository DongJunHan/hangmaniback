package com.project.hangmani.service;

import com.project.hangmani.winHistory.model.entity.WinHistory;
import com.project.hangmani.winHistory.service.WinHistoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;


import java.util.List;
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "classpath:application.properties"
})
@SpringBootTest
@Sql(value = {"classpath:drop.sql", "classpath:schema.sql", "classpath:data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class WinHistoryServiceTest {
    private final String sido = "인천";
    private final String sigugun = "부평구";
    @Autowired
    private WinHistoryService winHistoryService;
    @Test
    @DisplayName("H2 데이터베이스 당첨내역 데이터 확인")
    void WinHistoryListTest(){
        List<WinHistory> data = winHistoryService.getData(sido, sigugun);
        Assertions.assertThat(data.size()).isPositive();
    }
}