package com.project.hangmani.controller;

import com.project.hangmani.board.model.dto.*;
import com.project.hangmani.common.dto.Response;
import com.project.hangmani.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@Builder
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private Environment env;

    public BoardController(BoardService boardService, Environment env) {
        this.boardService = boardService;
        this.env = env;
    }
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Response<ResponseGetDTO> add(@RequestBody @Valid RequestInsertDTO boardDTO) throws IOException {
        ResponseGetDTO responseBoardDTO = boardService.insert(boardDTO);

        return Response.<ResponseGetDTO>builder()
                .data(responseBoardDTO)
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.name())
                .build();
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<ResponseDTO> deleteBoard(@RequestBody @Valid RequestDeleteDTO boardDTO) {
        ResponseDTO responseDeleteDTO = boardService.delete(boardDTO);

        return Response.<ResponseDTO>builder()
                .data(responseDeleteDTO)
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<List<ResponseGetDTO>> getBoards(@ModelAttribute RequestRangeDTO boardDTO) {
        return Response.<List<ResponseGetDTO>>builder()
                .data(boardService.getList(boardDTO))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();

    }
    @GetMapping(path = "/writer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<List<ResponseGetDTO>> getBoardsByWriter(@ModelAttribute RequestWriterDTO boardDTO) {
        return Response.<List<ResponseGetDTO>>builder()
                .data(boardService.getByWriter(boardDTO))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }
    @GetMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProfiles() {
        return Arrays.stream(env.getActiveProfiles()).findFirst().orElse("");
    }
}
