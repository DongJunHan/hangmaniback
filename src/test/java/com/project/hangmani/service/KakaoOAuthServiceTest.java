package com.project.hangmani.service;

import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
@SpringBootTest
class OAuthServiceTest {
    @Autowired
    private OAuthService oAuthService;
    private ConvertData convertData = new ConvertData();
    private AES aes;
    @BeforeEach
    void beforeEach() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AES.class);
        this.aes = ac.getBean(AES.class);
    }
    @Test
    void testInertUser() {
        String input = "{\"id\":1235586990,\"connected_at\":\"2023-03-18T08:03:31Z\",\"kakao_account\":{\"profile_needs_agreement\":true,\"has_email\":true,\"email_needs_agreement\":false,\"is_email_valid\":true,\"is_email_verified\":true,\"email\":\"gamedokdok@naver.com\",\"has_age_range\":true,\"age_range_needs_agreement\":false,\"age_range\":\"30~39\",\"has_birthday\":true,\"birthday_needs_agreement\":false,\"birthday\":\"1028\",\"birthday_type\":\"SOLAR\",\"has_gender\":true,\"gender_needs_agreement\":false,\"gender\":\"male\"}}";
        String access_token = "sDYY2gCTfYUbjh75nDpHuALiRkoVlgNQc6WkqgqeCj10mAAAAYb9mXwR";
//        kakaoOAuthService.InsertKaKaoUser(input, access_token);

    }

}