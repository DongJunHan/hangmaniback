package com.project.hangmani.repository;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.domain.User;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.exception.FailDeleteData;
import com.project.hangmani.security.AES;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties"
})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        initializers = {ConfigDataApplicationContextInitializer.class},
        classes = {AES.class, PropertiesValues.class}
)
class UserRepositoryTest {
    private UserRepository userRepository;
    @Autowired
    private AES aes;
    @Autowired
    private PropertiesValues propertiesValues;
    private static DataSource dataSource;
    private static JdbcTemplate template;
    private static DatabaseInit dbInit;

    @BeforeAll
    static void initOnce() {
        dbInit = new DatabaseInit();
        dataSource = dbInit.loadDataSource();
        template = dbInit.loadJdbcTemplate(dataSource);
    }
    @BeforeEach
    void TestConfig() {
        dbInit.loadScript(template);
        userRepository = new UserRepository(dataSource, aes, propertiesValues);

    }
    @Test
    @DisplayName("정상적인 OAuthID를 요청할 때")
    void findByoAuthIdSuccess() {
        //given
        String id = "2e8Q9cLX4wXejiS0aFuQVQ==";
//        String id = "1235586990";
        //when
        Optional<User> users = userRepository.findByoAuthId(id);
        //then
        assertThat(users.get().getOAuthID()).isEqualTo("2e8Q9cLX4wXejiS0aFuQVQ==");
    }
    @Test
    @DisplayName("없는 OAuthID를 요청할 때")
    void findByoAuthIdFail() {
//        String id = "2e8Q9cLX4wXejiS0aFuQVQ==";
        //given
        String id = "2e8Q9cLX4wXejiS0aFuQVQ2=";
        //when
        Optional<User> users = userRepository.findByoAuthId(id);
        //then
        assertThat(users.isEmpty()).isTrue();
    }
    @Test
    @DisplayName("정상적인 user id를 요청할 때")
    void findByIdSuccess() {
        Optional<User> user = userRepository.findById("id1");
        assertThat(user.get().getId()).isEqualTo("id1");
    }
    @Test
    @DisplayName("없는 user id를 요청할 때")
    void findByIdFail() {
        Optional<User> user = userRepository.findById("id2");
        assertThat(user.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("정상적인 유저 추가")
    void insertUserSuccess() {
        String userID = userRepository.insertUser(RequestInsertUserDTO.builder()
                .age("20")
                .email("tewest@naver.com")
                .gender("M")
                .oAuthID("dsfzcda")
                .oAuthType("kakao")
                .refreshToken("qqwefwgb")
                .build());
        Optional<User> user = userRepository.findById(userID);
        assertThat(user.get().getId()).isEqualTo(userID);
    }

    @Test
    @DisplayName("이미 있는 유저 추가")
    void insertUserFail() {
        assertThrows(DuplicateKeyException.class, () -> userRepository.insertUser(RequestInsertUserDTO.builder()
                .age("20")
                .email("tewest@naver.com")
                .gender("M")
                .oAuthID("1235586990")
                .oAuthType("kakao")
                .refreshToken("qqwefwgb")
                .build()));
    }

    @Test
    @DisplayName("유저 삭제 성공")
    void deleteUserSuccess() {
        int ret = userRepository.deleteUserById("id1");
        assertThat(ret).isEqualTo(2);
    }

    @Test
    @DisplayName("없는 유저 삭제")
    void deleteUserNotExist() {
        assertThrows(FailDeleteData.class, () -> userRepository.deleteUserById("noUser"));
    }
}