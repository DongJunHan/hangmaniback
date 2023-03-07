package com.project.hangmani.exception;

import com.project.hangmani.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(basePackages = "com.project.hangmani")
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(NotFoundUser.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto NotFoundUserException(RuntimeException e) {
        log.error("Error={}", e);
        return ResponseDto.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(FailDeleteData.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto FailDeleteDataException(RuntimeException e) {
        log.error("error={}",e);
        return ResponseDto.builder()
                .data("data:{\"rowNum\" : 0}")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
    }
}
