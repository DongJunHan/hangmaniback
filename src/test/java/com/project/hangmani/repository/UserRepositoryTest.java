package com.project.hangmani.repository;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.domain.User;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        utilMock = new Util();//Mockito.mock(Util.class);
        aes = new AES(utilMock);
        userRepository = new UserRepository(dataSource, utilMock, aes);

    }
    @Test
    void findByoAuthId() {
//        String id = "2e8Q9cLX4wXejiS0aFuQVQ==";
        //given
        String id = "1235586990";
        //when
        Optional<User> users = userRepository.findByoAuthId(id);
        //then
        assertThat(users.get().getOAuthID()).isEqualTo("2e8Q9cLX4wXejiS0aFuQVQ==");
    }
}