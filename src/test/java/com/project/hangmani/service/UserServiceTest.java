package com.project.hangmani.service;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.dto.UserDTO;
import com.project.hangmani.dto.UserDTO.ResponseUserDTO;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Slf4j
public class UserServiceTest {
    private static UserService userService;
    private ConvertData convertData = new ConvertData();
    private AES aes;
    @BeforeEach
    void TestConfig() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.register(Util.class);
        ac.register(AES.class);
        ac.refresh();

        Util util = ac.getBean(Util.class);
        aes = ac.getBean(AES.class);
        DatabaseInit dbInit = new DatabaseInit();
        DataSource dataSource = dbInit.loadDataSource("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_LOWER=TRUE", "sa", "");
        JdbcTemplate template = dbInit.loadJdbcTemplate(dataSource);
        dbInit.loadScript(template);

        UserRepository userRepository = new UserRepository(dataSource, util, aes);
        userService = new UserService(userRepository, aes);
        List<String> tables = template.queryForList
                ("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'hangmani'", String.class);
        log.info("table: {}", tables);
//        List<String> tables = template.queryForList("SHOW ALL TABLES", String.class);
        for (String t : tables) {
            log.info("tables: {}", t);
        }
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
