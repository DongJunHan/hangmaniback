package com.project.hangmani.service;

import com.project.hangmani.user.model.dto.RequestInsertDTO;
import com.project.hangmani.user.model.dto.ResponseDTO;
import com.project.hangmani.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "classpath:application.properties"
})
@SpringBootTest
@Sql(value = {"classpath:drop.sql", "classpath:schema.sql", "classpath:data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Test
    @DisplayName("새로운 유저 추가")
    void insertUserSuccess() {
        final String refreshToken = "refreshToken";
        ResponseDTO responseUserDTO = userService.InsertUser(RequestInsertDTO.builder()
                .age("20")
                .gender("M")
                .email("sadas@gmail.com")
                .oAuthType("kakao")
                .oAuthID("useroauthid")
                .refreshToken(refreshToken)
                .build());
//        byte[] bytes = convertData.base64ToByte(responseUserDTO.getO());
//        byte[] decryptData = aes.decryptData(bytes);
        Assertions.assertThat(refreshToken).isEqualTo(responseUserDTO.getRefreshToken());
    }

    @Test
    @DisplayName("동일한 유저 추가")
    void insertUserDuplicate() {
        final String refreshToken = "refreshToken";
        ResponseDTO responseUserDTO = userService.InsertUser(RequestInsertDTO.builder()
                .oAuthID("1235586990")
                .refreshToken(refreshToken)
                .build());
        Assertions.assertThat(responseUserDTO.getRefreshToken()).isEqualTo(refreshToken);
    }
}
