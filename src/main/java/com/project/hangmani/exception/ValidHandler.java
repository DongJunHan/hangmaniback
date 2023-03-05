package com.project.hangmani.exception;

import com.project.hangmani.dto.ResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestControllerAdvice
public class ValidHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto validExceptionHandler(MethodArgumentNotValidException e){
        log.error("err={}", e.getMessage());
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        if (errorList.size() == 0) {
            errorList.add(new ObjectError("Valid Error", "NO Message"));
        }

        return ResponseDto.builder()
                .data(null)
                .status(e.getStatusCode().value())
                .message(errorList.get(0).getDefaultMessage())
                .build();
    }
}
