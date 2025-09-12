package com.yukikase.framework.exceptions;

public class DatabaseStartupException extends RuntimeException {
    public DatabaseStartupException() {
        super();
    }

    public DatabaseStartupException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseStartupException(Throwable cause) {
        super(cause);
    }

    public DatabaseStartupException(String message) {
        super(message);
    }
}
