package com.project.hangmani.exception;

public class AlreadyExistStore extends NotFoundException{
    public AlreadyExistStore() {
        super("이미 존재하는 상점이 있습니다.");
    }

    public AlreadyExistStore(String message) {
        super(message);
    }

    public AlreadyExistStore(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistStore(Throwable cause) {
        super(cause);
    }
}
