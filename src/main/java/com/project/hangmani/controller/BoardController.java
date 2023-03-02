package com.project.hangmani.controller;

import com.project.hangmani.dto.DataResponseDTO;
import com.project.hangmani.domain.Board;
import com.project.hangmani.service.BoardService;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final ConvertData convertData;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
        this.convertData = new ConvertData();
    }
    @PostMapping("/board-insert")
    @ResponseBody
    public DataResponseDTO<Object> createBoard(@RequestBody Board board) {
        Board insertResult = boardService.createBoard(board);
        log.info("board={}",DataResponseDTO.of(insertResult,"success").getData());
        return DataResponseDTO.of(insertResult,"success");
    }
}
