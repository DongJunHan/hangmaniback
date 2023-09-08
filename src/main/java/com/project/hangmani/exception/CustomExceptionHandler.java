package com.project.hangmani.exception;

import com.project.hangmani.common.dto.Response;
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
    public Response NotFoundUserException(RuntimeException e) {
        log.error("Error", e);
        return Response.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(FailDeleteData.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response FailDeleteDataException(RuntimeException e) {
        log.error("error",e);
        return Response.builder()
                .data("data:{\"rowNum\" : 0}")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundStore.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NotFoundStoreException(RuntimeException e) {
        log.error("NotFoundStore", e);
        return Response.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundAttachment.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NotFoundAttachmentException(RuntimeException e) {
        log.error("NotFoundAttachment", e);
        return Response.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(FailInsertData.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response InsertException(RuntimeException e) {
        log.error("InsertFail", e);
        return Response.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(StringToJsonException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response StringToJsonException(RuntimeException e) {
        log.error("StringToJson", e);
        return Response.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(KO310Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response KO310Exception(RuntimeException e) {
        log.error("KO310Exception", e);
        return Response.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(KOE303Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response KOE303Exception(RuntimeException e) {
        log.error("KOE303Exception", e);
        return Response.builder()
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }


}
