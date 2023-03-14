package com.project.hangmani.exception;

import com.project.hangmani.dto.ResponseDTO;
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
    public ResponseDTO NotFoundUserException(RuntimeException e) {
        log.error("Error", e);
        return ResponseDTO.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(FailDeleteData.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO FailDeleteDataException(RuntimeException e) {
        log.error("error",e);
        return ResponseDTO.builder()
                .data("data:{\"rowNum\" : 0}")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundStore.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDTO NotFoundStoreException(RuntimeException e) {
        log.error("NotFoundStore", e);
        return ResponseDTO.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(FailInsertData.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDTO InsertException(RuntimeException e) {
        log.error("InsertFail", e);
        return ResponseDTO.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(StringToJsonException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDTO StringToJsonException(RuntimeException e) {
        log.error("StringToJson", e);
        return ResponseDTO.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }
}
