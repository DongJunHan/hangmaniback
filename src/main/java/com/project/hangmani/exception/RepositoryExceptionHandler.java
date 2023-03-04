package com.project.hangmani.exception;

import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.project.hangmani.enums.ResponseStatus.*;

@RestControllerAdvice//(basePackages = "com.project.hangmani.service")
@Slf4j
public class RepositoryExceptionHandler {
    @ExceptionHandler(NotFoundUser.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseBoardDTO NotFoundUserException(RuntimeException e) {
        e.printStackTrace();
        log.info("status={}",NOT_FOUND);
        return new ResponseBoardDTO(null, NOT_FOUND.getCode(), e.getMessage());
    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseBoardDTO DBConnectException(Exception e) {
        e.printStackTrace();
        return new ResponseBoardDTO(null, INTERNAL_SERER_ERROR.getCode(), "server not open");
    }
}
