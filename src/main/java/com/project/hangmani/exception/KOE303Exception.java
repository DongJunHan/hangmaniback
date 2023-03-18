package com.project.hangmani.exception;

public class KOE303Exception extends KakaoAuthException{
    public KOE303Exception() {
        super("KOE303 error");
    }

    public KOE303Exception(String message) {
        super(message);
    }

    public KOE303Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public KOE303Exception(Throwable cause) {
        super(cause);
    }
}
