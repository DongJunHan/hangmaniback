package com.project.hangmani.service;

import com.project.hangmani.board.model.dto.RequestInsertDTO;
import com.project.hangmani.board.model.dto.ResponseGetDTO;
import com.project.hangmani.board.service.BoardService;
import com.project.hangmani.exception.NotFoundUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "classpath:/application-test.properties"
})
@SpringBootTest
@Sql(value = {"classpath:drop.sql", "classpath:schema.sql", "classpath:data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class BoardServiceTest {
    @Autowired
    private BoardService boardService;
    @Test
    @DisplayName("게시물 삽입 성공")
    void BoardInsertTestSuccess() {
        //given
        RequestInsertDTO request = new RequestInsertDTO("공지사항12",
                "안녕하십니까 공지사항입니다^& </li>", "id1");
        //when
        ResponseGetDTO response = boardService.add(request);
        //then
        assertThat(response.getBoardContent()).isEqualTo(response.getBoardContent());
    }

    @Test
    @DisplayName("게시물 존재하지 않는 사용자")
    void BoardInsertTestFail() {
        //given
        RequestInsertDTO requestBoardDTO = new RequestInsertDTO(
                "공지사항12", "안녕하십니까 공지사항입니다^& </li>", "string");
        //then
        assertThatThrownBy(() -> boardService.add(requestBoardDTO))
                .isInstanceOf(NotFoundUser.class);
    }

}