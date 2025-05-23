package com.yukikase.diframework.exceptions;

public class BeanNotFoundException extends RuntimeException {
    public static final String EC_NO_BEANS_FOUND_OF_TYPE = "No beans found of type: %s";
    public static final String EC_NO_BEANS_FOUND_WITH_NAME = "No bean found of type: %s. With name: %s";

    public BeanNotFoundException(String message) {
        super(message);
    }

    public BeanNotFoundException() {
        super();
    }

    public BeanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanNotFoundException(Throwable cause) {
        super(cause);
    }

    protected BeanNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
