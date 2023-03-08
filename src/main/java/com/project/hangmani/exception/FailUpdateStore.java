package com.project.hangmani.exception;

public class FailUpdateStore extends InternalServerErrorException{
    public FailUpdateStore() {
        super("Failed to update resource");
    }

    public FailUpdateStore(String message) {
        super(message);
    }

    public FailUpdateStore(String message, Throwable cause) {
        super(message, cause);
    }

    public FailUpdateStore(Throwable cause) {
        super(cause);
    }
}
