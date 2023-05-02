package com.project.hangmani.exception;

public class FileIOException extends IO {
    public FileIOException() {
        super();
    }
    public FileIOException(Throwable cause){
        super(cause);
    }

    public FileIOException(String message,Throwable cause) {
        super(message, cause);
    }

}
