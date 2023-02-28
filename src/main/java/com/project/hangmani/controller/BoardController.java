package com.project.hangmani.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hangmani.model.management.Board;
import com.project.hangmani.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private ObjectMapper objectMapper = new ObjectMapper();
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    @PostMapping("/board-insert")
    @ResponseBody
    public String createBoard(@ModelAttribute Board board) {
        Board insertResult = boardService.createBoard(board);
        try {

            String result = objectMapper.writeValueAsString(insertResult);
            return result;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
