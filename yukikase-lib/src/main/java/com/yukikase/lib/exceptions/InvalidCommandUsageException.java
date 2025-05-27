package com.yukikase.lib.exceptions;

public class InvalidCommandUsageException extends RuntimeException {
    public InvalidCommandUsageException() {
        super();
    }

    public InvalidCommandUsageException(String message) {
        super(message);
    }

    public InvalidCommandUsageException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCommandUsageException(Throwable cause) {
        super(cause);
    }

    protected InvalidCommandUsageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
