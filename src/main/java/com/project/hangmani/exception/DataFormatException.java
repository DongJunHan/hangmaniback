package com.project.hangmani.exception;

public class DataFormatException extends RuntimeException {
    public DataFormatException() {
        super("data formatting fail");
    }

    public DataFormatException(String message) {
        super(message);
    }

    public DataFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
