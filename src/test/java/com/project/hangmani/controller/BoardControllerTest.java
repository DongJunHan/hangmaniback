package com.project.hangmani.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hangmani.board.model.dto.ResponseDTO;
import com.project.hangmani.board.model.dto.ResponseGetDTO;
import com.project.hangmani.common.Common;
import com.project.hangmani.common.dto.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "/application-test.properties"
})
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/drop.sql","/schema.sql","/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static Map<String, Class> classMap;
    private final Common common = new Common();
    public BoardControllerTest() {
        classMap = new HashMap<>();
        classMap.put("get", ResponseGetDTO.class);
        classMap.put("delete", ResponseDTO.class);
        classMap.put("boardList", List.class);
        classMap.put("int", Integer.class);
        classMap.put("error", ResponseDTO.class);
    }

    @Test
    @DisplayName("게시물 삽입 성공")
    void boardInsertSuccess() {
        //given
        String title = "제목";
        String content = "내용 </lt>!#";
        String writer = "id1";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("boardWriter", writer);
        paramMap.put("boardTitle", title);
        paramMap.put("boardContent", content);
        String json = common.convertMapToJson(paramMap);
        //when
        MvcResult mvcResult;
        try {
            mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/v1/board")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //then
        try {
            String response
                    = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            ResponseGetDTO board = (ResponseGetDTO)convertResponseToBoard(response, "get");
            org.assertj.core.api.Assertions.assertThat(board.getBoardContent()).isEqualTo(content);
            org.assertj.core.api.Assertions.assertThat(board.getBoardTitle()).isEqualTo(title);
            org.assertj.core.api.Assertions.assertThat(board.getBoardWriter()).isEqualTo(writer);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Object convertResponseToBoard(String responseBody, String key) throws JsonProcessingException {
        Class<?> convertValueType = classMap.get(key);
        ObjectMapper objectMapper = new ObjectMapper();
        Response<?> response = objectMapper.readValue(responseBody, Response.class);
        Object data = response.getData();
        if (convertValueType == ResponseGetDTO.class) {
            String dataJsonString = objectMapper.writeValueAsString(data);
            return objectMapper.readValue(dataJsonString, convertValueType);
        } else if (convertValueType == ResponseDTO.class) {
            return response;
        } else {
            return data;
        }
    }
}