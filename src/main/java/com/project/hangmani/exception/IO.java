package com.project.hangmani.exception;

public class IO extends RuntimeException {
    public IO() {
        super("io exception");
    }

    public IO(Throwable cause) {
        super(cause);
    }

    public IO(String message, Throwable cause) {
        super(message, cause);
    }
}
