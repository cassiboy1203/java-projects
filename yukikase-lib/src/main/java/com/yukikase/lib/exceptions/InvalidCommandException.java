package com.yukikase.lib.exceptions;

public class InvalidCommandException extends RuntimeException {
    public static final String COMMAND_NOT_FOUND = "Command with name: %s. Not found.";

    public InvalidCommandException() {
        super();
    }

    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCommandException(Throwable cause) {
        super(cause);
    }

    protected InvalidCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
