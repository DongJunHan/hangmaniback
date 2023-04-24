package com.project.hangmani.exception;

public class SecurityException extends RuntimeException{
    public SecurityException() {
        super("secure exception");
    }

    public SecurityException(String noPublicKey) {
        super();
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
