package com.project.hangmani.exception;



public class NotFoundUser extends NotFoundException{
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
