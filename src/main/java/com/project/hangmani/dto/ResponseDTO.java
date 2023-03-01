package com.project.hangmani.dto;

//import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
//import lombok.Setter;

//@Setter
//@Getter
@RequiredArgsConstructor
public class ResponseDTO {
    private final HttpStatus resultCode;
    private final String message;

    public static ResponseDTO of(HttpStatus resultCode, String message) {
        return new ResponseDTO(resultCode, message);
    }
    public static ResponseDTO of(HttpStatus resultCode, Exception e) {
        return new ResponseDTO(resultCode, e.getMessage());
    }

}
