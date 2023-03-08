package com.project.hangmani.exception;

import com.project.hangmani.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ValidHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO validExceptionHandler(MethodArgumentNotValidException e){
        log.error("err={}", e.getMessage());
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        if (errorList.size() == 0) {
            errorList.add(new ObjectError("Valid Error", "NO Message"));
        }

        return ResponseDTO.builder()
                .data(null)
                .status(e.getStatusCode().value())
                .message(errorList.get(0).getDefaultMessage())
                .build();
    }
}
