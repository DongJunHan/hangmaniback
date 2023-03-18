package com.project.hangmani.exception;

public class KakaoAuthException extends RuntimeException{
    public KakaoAuthException() {
        super("kakao api error");
    }

    public KakaoAuthException(String message) {
        super(message);
    }

    public KakaoAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public KakaoAuthException(Throwable cause) {
        super(cause);
    }
}
