package com.project.hangmani.repository;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.domain.User;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.exception.FailDeleteData;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class UserRepositoryTest {
    private UserRepository userRepository;
    @BeforeEach
    void TestConfig() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(Util.class);
        Util utilMock = ac.getBean(Util.class);
        DatabaseInit dbInit = new DatabaseInit();
        DataSource dataSource = dbInit.loadDataSource("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_LOWER=TRUE", "sa", "");
        JdbcTemplate template = dbInit.loadJdbcTemplate(dataSource);
        dbInit.loadScript(template);
        AES aes = new AES(utilMock);
        userRepository = new UserRepository(dataSource, utilMock, aes);

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