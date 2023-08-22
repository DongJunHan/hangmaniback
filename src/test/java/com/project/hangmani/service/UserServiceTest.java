package com.project.hangmani.service;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.dto.UserDTO;
import com.project.hangmani.dto.UserDTO.ResponseUserDTO;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties"
})
@ContextConfiguration(
        initializers = {ConfigDataApplicationContextInitializer.class},
        classes = {AES.class, PropertiesValues.class}
)
public class UserServiceTest {
    private static UserService userService;
    private ConvertData convertData;
    @Autowired
    private AES aes;
    @Autowired
    private PropertiesValues propertiesValues;
    private static DataSource dataSource;
    private static JdbcTemplate template;
    private static DatabaseInit dbInit;

    @SneakyThrows
    @BeforeAll
    static void initOnce() {
        dbInit = new DatabaseInit();
        dataSource = dbInit.loadDataSource();
        template = dbInit.loadJdbcTemplate(dataSource);
    }
    @BeforeEach
    void TestConfig() {
        convertData = new ConvertData(propertiesValues);
        dbInit.loadScript(template);
        UserRepository userRepository = new UserRepository(dataSource, aes, propertiesValues);
        userService = new UserService(userRepository, aes, propertiesValues);
//        String packageName = Util.class.getPackage().getName();
//        String className = Util.class.getSimpleName();
//        String qualifiedClassName = packageName + "." + className;
        // 사용자 정의 함수 등록
//        template.execute("CREATE ALIAS IF NOT EXISTS get_distance FOR \"" + qualifiedClassName + ".getDistance\";");
    }

    @Test
    @DisplayName("새로운 유저 추가")
    void insertUserSuccess() {
        final String refreshToken = "refreshToken";
        ResponseUserDTO responseUserDTO = userService.InsertUser(UserDTO.RequestInsertUserDTO.builder()
                .age("20")
                .gender("M")
                .email("sadas@gmail.com")
                .oAuthType("kakao")
                .oAuthID("useroauthid")
                .refreshToken(refreshToken)
                .build());
        byte[] bytes = convertData.base64ToByte(responseUserDTO.getRefreshToken());
        byte[] decryptData = aes.decryptData(bytes);
        Assertions.assertThat(convertData.byteToString(decryptData, StandardCharsets.UTF_8)).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("동일한 유저 추가")
    void insertUserDuplicate() {
        final String refreshToken = "refreshToken";
        ResponseUserDTO responseUserDTO = userService.InsertUser(UserDTO.RequestInsertUserDTO.builder()
                .oAuthID("1235586990")
                .refreshToken(refreshToken)
                .build());
        Assertions.assertThat(responseUserDTO.getRefreshToken()).isEqualTo(refreshToken);
    }
}
