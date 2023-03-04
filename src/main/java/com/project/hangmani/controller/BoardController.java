package com.project.hangmani.controller;

import com.project.hangmani.dto.BoardDTO.RequestBoardDTO;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    @PostMapping("/board-insert")
    @ResponseBody
    public ResponseBoardDTO createBoard(@RequestBody RequestBoardDTO boardDTO) {
        RequestBoardDTO s = new RequestBoardDTO();
        ResponseBoardDTO responseDTO = boardService.createBoard(boardDTO);
        return responseDTO;
    }
}
