package com.project.hangmani.service;

import com.project.hangmani.oauth.service.OAuth;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;


import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "classpath:application.properties"
})
@SpringBootTest
@Sql(value = {"classpath:drop.sql", "classpath:schema.sql", "classpath:data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class OAuthServiceTest {
    @Autowired
    private OAuth oAuthService;
    @Autowired
    private ConvertData convertData;
    @Test
    @DisplayName("OAuth Insert")
    void testInertUser() {
        String input = "{\"id\":1235586990,\"connected_at\":\"2023-03-18T08:03:31Z\",\"kakao_account\":{\"profile_needs_agreement\":true,\"has_email\":true,\"email_needs_agreement\":false,\"is_email_valid\":true,\"is_email_verified\":true,\"email\":\"gamedokdok@naver.com\",\"has_age_range\":true,\"age_range_needs_agreement\":false,\"age_range\":\"30~39\",\"has_birthday\":true,\"birthday_needs_agreement\":false,\"birthday\":\"1028\",\"birthday_type\":\"SOLAR\",\"has_gender\":true,\"gender_needs_agreement\":false,\"gender\":\"male\"}}";
        String access_token = "sDYY2gCTfYUbjh75nDpHuALiRkoVlgNQc6WkqgqeCj10mAAAAYb9mXwR";
//        oAuthService.
    }

}