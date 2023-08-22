package com.project.hangmani.service;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.dto.BoardDTO.RequestBoardInsertDTO;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.repository.BoardRepository;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.security.AES;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties"
})
@ContextConfiguration(
        initializers = {ConfigDataApplicationContextInitializer.class},
        classes = {AES.class, PropertiesValues.class}
)
class BoardServiceTest {
    private BoardService boardService;
    private static DataSource dataSource;
    private static JdbcTemplate template;
    private static DatabaseInit dbInit;
    @Autowired
    private AES aes;
    @Autowired
    private PropertiesValues propertiesValues;
    @BeforeAll
    static void initOnce() {
        dbInit = new DatabaseInit();
        dataSource = dbInit.loadDataSource();
        template = dbInit.loadJdbcTemplate(dataSource);
    }
    @BeforeEach
    void TestConfig() {
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