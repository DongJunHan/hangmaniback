package com.project.hangmani.exception;

public class NotFoundStore extends NotFoundException{
    public NotFoundStore() {
        super("상점정보를 찾을 수 없습니다.");
    }

    public NotFoundStore(String message) {
        super(message);
    }

    public NotFoundStore(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundStore(Throwable cause) {
        super(cause);
    }
}
