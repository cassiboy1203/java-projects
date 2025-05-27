package com.yukikase.lib.exceptions;

public class NoPermissionFoundException extends RuntimeException {
    public static String CLASS_DOES_NOT_HAVE_PERMISSION = "Class %s does not have permission";
    public static String METHOD_DOES_NOT_HAVE_PERMISSION = "Method %s does not have permission";

    public NoPermissionFoundException() {
        super();
    }

    public NoPermissionFoundException(String message) {
        super(message);
    }

    public NoPermissionFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionFoundException(Throwable cause) {
        super(cause);
    }

    protected NoPermissionFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
