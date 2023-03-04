package com.project.hangmani.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;
public enum ResponseStatus {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERER_ERROR(500);
    private final Integer code;

    ResponseStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
