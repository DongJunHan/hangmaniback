package com.project.hangmani.controller;

import com.project.hangmani.dto.BoardDTO;
import com.project.hangmani.dto.BoardDTO.*;
import com.project.hangmani.dto.ResponseDTO;
import com.project.hangmani.service.BoardService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@Builder
@Slf4j
public class BoardController {
    private final BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    @PostMapping
    @ResponseBody
    public ResponseDTO<ResponseBoardDTO> createBoard(@RequestBody @Valid RequestBoardInsertDTO boardDTO) {
        ResponseBoardDTO responseBoardDTO = boardService.createBoard(boardDTO);

        return ResponseDTO.<ResponseBoardDTO>builder()
                .data(responseBoardDTO)
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

    @DeleteMapping
    @ResponseBody
    public ResponseDTO<ResponseDeleteDTO> deleteBoard(@RequestBody @Valid RequestDeleteDTO boardDTO) {
        ResponseDeleteDTO responseDeleteDTO = boardService.deleteBoard(boardDTO);

        return ResponseDTO.<ResponseDeleteDTO>builder()
                .data(responseDeleteDTO)
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

    @GetMapping
    @ResponseBody
    public ResponseDTO<List<ResponseBoardDTO>> getBoards(@ModelAttribute RequestBoardDTO boardDTO) {
        return ResponseDTO.<List<ResponseBoardDTO>>builder()
                .data(boardService.getBoards(boardDTO))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();

    }
    @GetMapping("/writer")
    @ResponseBody
    public ResponseDTO<List<ResponseBoardDTO>> getBoardsByWriter(@ModelAttribute RequestBoardDTO boardDTO) {
        return ResponseDTO.<List<ResponseBoardDTO>>builder()
                .data(boardService.getBoardsByWriter(boardDTO))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

}
