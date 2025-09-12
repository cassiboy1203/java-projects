package com.yukikase.framework.exceptions;

public class EntityCreationException extends RuntimeException {
    public EntityCreationException() {
        super();
    }

    public EntityCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityCreationException(Throwable cause) {
        super(cause);
    }

    public EntityCreationException(String message) {
        super(message);
    }
}
