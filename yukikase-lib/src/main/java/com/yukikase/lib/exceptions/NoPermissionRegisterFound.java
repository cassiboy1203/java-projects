package com.yukikase.lib.exceptions;

public class NoPermissionRegisterFound extends RuntimeException {
    public NoPermissionRegisterFound() {
        super();
    }

    public NoPermissionRegisterFound(String message) {
        super(message);
    }

    public NoPermissionRegisterFound(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionRegisterFound(Throwable cause) {
        super(cause);
    }
}
