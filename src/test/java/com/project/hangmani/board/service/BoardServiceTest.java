package com.project.hangmani.board.service;

import com.project.hangmani.board.model.dto.RequestInsertDTO;
import com.project.hangmani.board.model.dto.ResponseGetDTO;
import com.project.hangmani.board.service.BoardService;
import com.project.hangmani.exception.NotFoundUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;


import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "/application-test.properties"
})
@SpringBootTest
@Sql(value = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BoardServiceTest {
    @Autowired
    private BoardService boardService;
    @Test
    @DisplayName("게시물 삽입 성공")
    void BoardInsertTestSuccess() throws IOException {
        //given
        RequestInsertDTO request = new RequestInsertDTO("공지사항12",
                "안녕하십니까 공지사항입니다^& </li>", "id1", new ArrayList<>());
        //when
        ResponseGetDTO response = boardService.insert(request);
        //then
        assertThat(response.getBoardContent()).isEqualTo(response.getBoardContent());
    }

    @Test
    @DisplayName("게시물 존재하지 않는 사용자")
    void BoardInsertTestFail() {
        //given
        RequestInsertDTO requestBoardDTO = new RequestInsertDTO(
                "공지사항12", "안녕하십니까 공지사항입니다^& </li>", "string",new ArrayList<>());
        //then
        assertThatThrownBy(() -> boardService.insert(requestBoardDTO))
                .isInstanceOf(NotFoundUser.class);
    }

}