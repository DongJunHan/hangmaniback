package com.project.hangmani.exception;

public class NotFoundReport extends NotFoundException{
    public NotFoundReport() {
        super("신고정보를 찾을 수 없습니다.");
    }

    public NotFoundReport(String message) {
        super(message);
    }

    public NotFoundReport(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundReport(Throwable cause) {
        super(cause);
    }
}
