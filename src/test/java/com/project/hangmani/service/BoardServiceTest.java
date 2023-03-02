package com.project.hangmani.service;

import com.project.hangmani.domain.Board;
import com.project.hangmani.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

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
        @Bean
        BoardService BoardService1() {
            return new BoardService(boardRepository());
        }

    }
    @Test
    @DisplayName("H2 데이터베이스 게시물 삽입")
    void StoreInfoTest(){
        Board board = new Board();
        board.setBoardWriter("관리자1");
        board.setTitle("공지사항12");
        board.setContent("안녕하십니까 공지사항입니다^& </li>");
        Board boardResult = boardService.createBoard(board);
        log.info("result={}",boardResult);
        assertThat(boardResult).isEqualTo(board);
    }
}