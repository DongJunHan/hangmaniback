package com.project.hangmani.service;

import com.project.hangmani.dto.BoardDTO.RequestBoardInsertDTO;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.repository.BoardRepository;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class BoardServiceTest {
    @Autowired
    private BoardService boardService;

    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;
        private final Util util;
        private final AES aes;
        TestConfig(DataSource dataSource, Util util, AES aes) {
            this.dataSource = dataSource;
            this.util = util;
            this.aes = aes;
        }

        BoardRepository boardRepository() {
            return new BoardRepository(dataSource);
        }

        UserRepository userRepository() {
            return new UserRepository(dataSource, util, aes);
        }

        @Bean
        BoardService BoardService1() {
            return new BoardService(boardRepository(), userRepository(),this.util);
        }
    }

//    @BeforeEach
//    void beforeEach() {
//
//    }

    @Test
    @DisplayName("H2 데이터베이스 게시물 삽입 성공")
    void BoardInsertTestSuccess() {
        RequestBoardInsertDTO requestBoardDTO = new RequestBoardInsertDTO("공지사항12", "안녕하십니까 공지사항입니다^& </li>", "id1");
        ResponseBoardDTO response = boardService.createBoard(requestBoardDTO);
        log.info("result={}", response);
        assertThat(response.getBoardContent()).isEqualTo(requestBoardDTO.getBoardContent());
    }

    @Test
    @DisplayName("H2 데이터베이스 게시물 존재하지 않는 사용자")
    void BoardInsertTestFail() {
        RequestBoardInsertDTO requestBoardDTO = new RequestBoardInsertDTO("공지사항12", "안녕하십니까 공지사항입니다^& </li>", "string");
        assertThatThrownBy(() -> boardService.createBoard(requestBoardDTO))
                .isInstanceOf(NotFoundUser.class);
    }

}