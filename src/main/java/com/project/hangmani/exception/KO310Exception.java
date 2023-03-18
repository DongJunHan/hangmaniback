package com.project.hangmani.exception;

public class KO310Exception extends KakaoAuthException{
    public KO310Exception() {
        super("KO310 error");
    }

    public KO310Exception(String message) {
        super(message);
    }

    public KO310Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public KO310Exception(Throwable cause) {
        super(cause);
    }
}
