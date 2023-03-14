package com.project.hangmani.exception;

public class StringToJsonException extends DataFormatException{
    public StringToJsonException() {
        super("String to Json convert Fail");
    }

    public StringToJsonException(String message) {
        super(message);
    }

    public StringToJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
