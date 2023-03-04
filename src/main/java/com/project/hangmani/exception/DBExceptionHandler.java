package com.project.hangmani.exception;

import com.project.hangmani.dto.BoardDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;

import static com.project.hangmani.enums.ResponseStatus.INTERNAL_SERER_ERROR;
@RestControllerAdvice
public class DBExceptionHandler {
    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BoardDTO.ResponseBoardDTO DBConnectException(Exception e) {
        e.printStackTrace();
        return new BoardDTO.ResponseBoardDTO(null, INTERNAL_SERER_ERROR.getCode(), "server not open");
    }
}
