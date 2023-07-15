package com.project.hangmani.repository;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.domain.User;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class UserRepositoryTest {
    private UserRepository userRepository;
    private static AES aes;
    private ConvertData convertData = new ConvertData();
    private DataSource dataSource;
    private Util utilMock;
    private JdbcTemplate template;
    @BeforeEach
    void TestConfig() {
        DatabaseInit dbInit = new DatabaseInit();
        dataSource = dbInit.loadDataSource("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_LOWER=TRUE", "sa", "");
        template = dbInit.loadJdbcTemplate(dataSource);
        dbInit.loadScript(template);
        utilMock = new Util();
        aes = new AES(utilMock);
        userRepository = new UserRepository(dataSource, utilMock, aes);

    }
    @Test
    @DisplayName("정상적인 OAuthID를 요청할 때")
    void findByoAuthId() {
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
    void failFindByoAuthId() {
//        String id = "2e8Q9cLX4wXejiS0aFuQVQ==";
        //given
        String id = "2e8Q9cLX4wXejiS0aFuQVQ2=";
        //when
        Optional<User> users = userRepository.findByoAuthId(id);
        //then
        assertThrows(NoSuchElementException.class, () -> users.get().getOAuthID());
    }
}