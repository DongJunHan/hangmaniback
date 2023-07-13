package com.project.hangmani.repository;

import com.project.hangmani.domain.User;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class UserRepositoryTest {
    private static UserRepository userRepository;
    private static AES aes;
    private ConvertData convertData = new ConvertData();
    private static DataSource dataSource;
    private static Util utilMock;
    private static JdbcTemplate template;
    @BeforeAll
    static void preDB() {
        dataSource = new DriverManagerDataSource("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_LOWER=TRUE","sa","");
        utilMock = new Util();//Mockito.mock(Util.class);
        aes = new AES(utilMock);
        userRepository = new UserRepository(dataSource, utilMock, aes);

        template = new JdbcTemplate(dataSource);
        Resource schema = new ClassPathResource("schema.sql");
        Resource data = new ClassPathResource("data.sql");
        try {
            ScriptUtils.executeSqlScript(template.getDataSource().getConnection(), schema);
            ScriptUtils.executeSqlScript(template.getDataSource().getConnection(), data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String packageName = Util.class.getPackage().getName();
        String className = Util.class.getSimpleName();
        String qualifiedClassName = packageName + "." + className;

        // 사용자 정의 함수 등록
        template.execute("CREATE ALIAS IF NOT EXISTS get_distance FOR \"" + qualifiedClassName + ".getDistance\";");
    }
    @Test
    void findByoAuthId() {
//        String id = "2e8Q9cLX4wXejiS0aFuQVQ==";
        String id = "1235586990";
        Optional<User> users = userRepository.findByoAuthId(id);
        log.info("users: {}", users);
        assertThat(users.get().getOAuthID()).isEqualTo("2e8Q9cLX4wXejiS0aFuQVQ==");
    }

    @Test
    void findById() {
    }
}