package com.project.hangmani.exception;

public class FailInsertData extends NotFoundException{
    public FailInsertData() {
        super("insert store fail");
    }

    public FailInsertData(String message) {
        super(message);
    }

    public FailInsertData(String message, Throwable cause) {
        super(message, cause);
    }

    public FailInsertData(Throwable cause) {
        super(cause);
    }
}
