package com.project.hangmani.exception;

import com.project.hangmani.common.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;

@RestControllerAdvice
@Slf4j
public class DBExceptionHandler {
    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response DBConnectException(Exception e) {
        log.error("error", e);
        return Response.builder()
                .data(null)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("server not open")
                .build();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response EmptyResultException(Exception e) {
        log.error("EmptyResultException", e);
        return Response.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.name())
                .build();
    }

    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response ResultSizeException(Exception e) {
        log.error("ResultSizeException", e);
        return Response.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.name())
                .build();
    }
}
