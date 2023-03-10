package com.project.hangmani.service;

import com.project.hangmani.dto.BoardDTO.RequestBoardDTO;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.repository.BoardRepository;
import com.project.hangmani.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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

        TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        BoardRepository boardRepository() {
            return new BoardRepository(dataSource);
        }

        UserRepository userRepository() {
            return new UserRepository(dataSource);
        }

        @Bean
        BoardService BoardService1() {
            return new BoardService(boardRepository(), userRepository());
        }
    }

    @AfterEach
    void afterEach() {

    }

    @Test
    @DisplayName("H2 데이터베이스 게시물 삽입 성공")
    void BoardInsertTestSuccess() {
        RequestBoardDTO requestBoardDTO = new RequestBoardDTO("공지사항12", "안녕하십니까 공지사항입니다^& </li>", "id1");
        ResponseBoardDTO response = boardService.createBoard(requestBoardDTO);
        log.info("result={}", response);
        assertThat(response.getBoardContent()).isEqualTo(requestBoardDTO.getBoardContent());
    }

    @Test
    @DisplayName("H2 데이터베이스 게시물 존재하지 않는 사용자")
    void BoardInsertTestFail() {
        RequestBoardDTO requestBoardDTO = new RequestBoardDTO("공지사항12", "안녕하십니까 공지사항입니다^& </li>", "string");
        assertThatThrownBy(() -> boardService.createBoard(requestBoardDTO))
                .isInstanceOf(NotFoundUser.class);
    }

}