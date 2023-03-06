package com.project.hangmani.controller;

import com.project.hangmani.dto.BoardDTO.RequestBoardDTO;
import com.project.hangmani.dto.BoardDTO.RequestDeleteDTO;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.dto.BoardDTO.ResponseDeleteDTO;
import com.project.hangmani.dto.ResponseDto;
import com.project.hangmani.service.BoardService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@Builder
@Slf4j
public class BoardController {
    private final BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    @PostMapping
    @ResponseBody
    public ResponseDto<ResponseBoardDTO> createBoard(@RequestBody @Valid RequestBoardDTO boardDTO) {
        ResponseBoardDTO responseBoardDTO = boardService.createBoard(boardDTO);

        return ResponseDto.<ResponseBoardDTO>builder()
                .data(responseBoardDTO)
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

    @DeleteMapping
    @ResponseBody
    public ResponseDto<ResponseDeleteDTO> deleteBoard(@RequestBody @Valid RequestDeleteDTO boardDTO) {
        ResponseDeleteDTO responseDeleteDTO = boardService.deleteBoard(boardDTO);

        return ResponseDto.<ResponseDeleteDTO>builder()
                .data(responseDeleteDTO)
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }
}
