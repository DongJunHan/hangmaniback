package com.project.hangmani.exception;

public class InvalidKeySpec extends SecurityException{
    public InvalidKeySpec() {
        super("key spec invalid");
    }

    public InvalidKeySpec(String message, Throwable cause) {
        super(message, cause);
    }
}
