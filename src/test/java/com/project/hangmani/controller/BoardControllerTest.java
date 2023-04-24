package com.project.hangmani.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(BoardController.class)
class BoardControllerTest {
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        boardTitle = "";
        boardContent = "내용입니다.</li> &gt";
        boardWriter = "id1";
    }

}