package com.project.hangmani.exception;

import com.project.hangmani.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseDto DBConnectException(Exception e) {
        log.error("error={}", e);
        return ResponseDto.builder()
                .data(null)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("server not open")
                .build();
    }
}
