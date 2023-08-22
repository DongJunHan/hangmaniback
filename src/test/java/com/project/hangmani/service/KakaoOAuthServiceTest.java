package com.project.hangmani.service;

import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
//@Import({OAuthService.class, AES.class, PropertiesValues.class, Util.class})
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties"
})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        initializers = {ConfigDataApplicationContextInitializer.class},
        classes = {PropertiesValues.class, AES.class}
)
class OAuthServiceTest {
    //@Autowired
    private OAuthService oAuthService;
    private ConvertData convertData;
    @Autowired
    private AES aes;
    @Autowired
    private PropertiesValues propertiesValues;
    @Test
    @DisplayName("OAuth Insert")
    void testInertUser() {
        String input = "{\"id\":1235586990,\"connected_at\":\"2023-03-18T08:03:31Z\",\"kakao_account\":{\"profile_needs_agreement\":true,\"has_email\":true,\"email_needs_agreement\":false,\"is_email_valid\":true,\"is_email_verified\":true,\"email\":\"gamedokdok@naver.com\",\"has_age_range\":true,\"age_range_needs_agreement\":false,\"age_range\":\"30~39\",\"has_birthday\":true,\"birthday_needs_agreement\":false,\"birthday\":\"1028\",\"birthday_type\":\"SOLAR\",\"has_gender\":true,\"gender_needs_agreement\":false,\"gender\":\"male\"}}";
        String access_token = "sDYY2gCTfYUbjh75nDpHuALiRkoVlgNQc6WkqgqeCj10mAAAAYb9mXwR";
//        kakaoOAuthService.InsertKaKaoUser(input, access_token);

    }

}