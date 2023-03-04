package com.project.hangmani.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundUser extends RuntimeException{
    public NotFoundUser() {
        super("존재하지 않는 사용자 입니다.");
    }

    public NotFoundUser(String message) {
        super(message);
    }

    public NotFoundUser(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundUser(Throwable cause) {
        super(cause);
    }

    protected NotFoundUser(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
