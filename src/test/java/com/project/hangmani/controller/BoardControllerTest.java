package com.project.hangmani.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.hangmani.board.model.dto.ResponseDTO;
import com.project.hangmani.board.model.dto.ResponseGetDTO;
import com.project.hangmani.board.service.BoardService;
import com.project.hangmani.common.dto.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(BoardController.class)
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BoardService boardService;
    @Autowired
    DataSource dataSource;
    private static Map<String, Class> classMap;
    public BoardControllerTest() {
        classMap = new HashMap<>();
        classMap.put("get", ResponseGetDTO.class);
        classMap.put("delete", ResponseDTO.class);
        classMap.put("boardList", List.class);
        classMap.put("int", Integer.class);
        classMap.put("error", ResponseDTO.class);
    }
    @AfterEach
    void init() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        ClassPathResource drop = new ClassPathResource("drop.sql");
        try {
            ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), drop);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ClassPathResource schema = new ClassPathResource("schema.sql");
        try {
            ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), schema);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ClassPathResource data = new ClassPathResource("data.sql");
        try {
            ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("게시물 삽입 성공")
    void boardInsertSuccess() {
        //given
        String title = "제목";
        String content = "내용 </lt>!#";
        String writer = "id1";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("memberID", writer);
        paramMap.put("title", title);
        paramMap.put("content", content);
        String json = convertMapToJson(paramMap);
        //when
        MvcResult mvcResult;
        try {
            mvcResult = mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/v1/board")
                    .content(json))
                    .andExpect(status().isOk())
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
    private String convertMapToJson(Map param) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json;
        try {
            json = objectMapper.writeValueAsString(param);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}