package com.project.hangmani.exception;

import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.project.hangmani.enums.ResponseStatus.*;

@RestControllerAdvice(basePackages = "com.project.hangmani")
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(NotFoundUser.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseBoardDTO NotFoundUserException(RuntimeException e) {
        log.error("Error={}",e);
        return new ResponseBoardDTO(null, NOT_FOUND.getCode(), e.getMessage());
    }


}
