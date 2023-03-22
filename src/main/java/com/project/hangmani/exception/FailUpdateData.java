package com.project.hangmani.exception;

public class FailUpdateData extends NotFoundException{
    public FailUpdateData() {
        super("fail update data");
    }

    public FailUpdateData(String message) {
        super(message);
    }

    public FailUpdateData(String message, Throwable cause) {
        super(message, cause);
    }

    public FailUpdateData(Throwable cause) {
        super(cause);
    }

    protected FailUpdateData(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
