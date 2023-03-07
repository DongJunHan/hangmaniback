package com.project.hangmani.exception;

public class FailDeleteData extends InternalServerErrorException{
    public FailDeleteData() {
        super("Failed to delete resource");
    }

    public FailDeleteData(String message) {
        super(message);
    }

    public FailDeleteData(String message, Throwable cause) {
        super(message, cause);
    }

    public FailDeleteData(Throwable cause) {
        super(cause);
    }
}
