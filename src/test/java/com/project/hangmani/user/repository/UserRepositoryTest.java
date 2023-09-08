package com.project.hangmani.user.repository;

import com.project.hangmani.exception.FailDeleteData;
import com.project.hangmani.user.model.dto.RequestInsertDTO;
import com.project.hangmani.user.model.dto.UserDTO;
import com.project.hangmani.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "/application-test.properties"
})
@SpringBootTest
@Sql(value = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    @DisplayName("정상적인 OAuthID를 요청할 때")
    void findByoAuthIdSuccess() {
        //given
        String id = "2e8Q9cLX4wXejiS0aFuQVQ==";
//        String id = "1235586990";
        //when
        Optional<UserDTO> users = userRepository.getByoAuthId(id);
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
        Optional<UserDTO> users = userRepository.getByoAuthId(id);
        //then
        assertThat(users.isEmpty()).isTrue();
    }
    @Test
    @DisplayName("정상적인 user id를 요청할 때")
    void findByIdSuccess() {
        Optional<UserDTO> user = userRepository.getById("id1");
        assertThat(user.get().getId()).isEqualTo("id1");
    }
    @Test
    @DisplayName("없는 user id를 요청할 때")
    void findByIdFail() {
        Optional<UserDTO> user = userRepository.getById("id2");
        assertThat(user.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("정상적인 유저 추가")
    void insertUserSuccess() {
        //given
        RequestInsertDTO dto = RequestInsertDTO.builder()
                .age("20")
                .email("tewest@naver.com")
                .gender("M")
                .oAuthID("dsfzcda")
                .oAuthType("kakao")
                .refreshToken("qqwefwgb")
                .build();
        //when
        String userID = userRepository.add(dto.convertToEntity());
        Optional<UserDTO> user = userRepository.getById(userID);
        //then
        assertThat(user.get().getId()).isEqualTo(userID);
    }
    @Test
    @DisplayName("유저 삭제 성공")
    void deleteUserSuccess() {
        int ret = userRepository.delete("id1");
        assertThat(ret).isEqualTo(2);
    }

    @Test
    @DisplayName("없는 유저 삭제")
    void deleteUserNotExist() {
        assertThrows(FailDeleteData.class, () -> userRepository.delete("noUser"));
    }
}