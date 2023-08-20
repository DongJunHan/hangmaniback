package com.project.hangmani.service;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.dto.BoardDTO.RequestBoardInsertDTO;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.repository.BoardRepository;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
//@SpringBootTest
class BoardServiceTest {
    private BoardService boardService;
    private JdbcTemplate template;
    private DataSource dataSource;

    @BeforeEach
    void TestConfig() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.register(PropertiesValues.class);
        ac.register(AES.class);
        ac.refresh();
        PropertiesValues propertiesValues = ac.getBean(PropertiesValues.class);
        AES aes = ac.getBean(AES.class);


        //;MODE=MySQL;DATABASE_TO_LOWER=TRUE
        DatabaseInit dbInit = new DatabaseInit();
        dataSource = dbInit.loadDataSource("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1", "sa", "");
        template = dbInit.loadJdbcTemplate(dataSource);
        dbInit.loadScript(template);

        UserRepository userRepository = new UserRepository(dataSource, aes, propertiesValues);
        BoardRepository boardRepository = new BoardRepository(dataSource, propertiesValues);
        boardService = new BoardService(boardRepository, userRepository, propertiesValues);
    }

//    @BeforeEach
//    void beforeEach() {
//
//    }

    @Test
    @DisplayName("게시물 삽입 성공")
    void BoardInsertTestSuccess() {
        RequestBoardInsertDTO requestBoardDTO = new RequestBoardInsertDTO("공지사항12", "안녕하십니까 공지사항입니다^& </li>", "id1");
        ResponseBoardDTO response = boardService.createBoard(requestBoardDTO);
        assertThat(response.getBoardContent()).isEqualTo(requestBoardDTO.getBoardContent());
    }

    @Test
    @DisplayName("게시물 존재하지 않는 사용자")
    void BoardInsertTestFail() {
        RequestBoardInsertDTO requestBoardDTO = new RequestBoardInsertDTO("공지사항12", "안녕하십니까 공지사항입니다^& </li>", "string");
        assertThatThrownBy(() -> boardService.createBoard(requestBoardDTO))
                .isInstanceOf(NotFoundUser.class);
    }

}