package com.project.hangmani.repository;

import com.project.hangmani.domain.User;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AES aes;
    private ConvertData convertData = new ConvertData();

    @Test
    void findByoAuthId() {
//        String id = "2e8Q9cLX4wXejiS0aFuQVQ==";
        String id = "1235586990";
        Optional<User> users = userRepository.findByoAuthId(id);
        assertThat(users.get().getOAuthID()).isEqualTo("2e8Q9cLX4wXejiS0aFuQVQ==");
    }

    @Test
    void findById() {
    }
}