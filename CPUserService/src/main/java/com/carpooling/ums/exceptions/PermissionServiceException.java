package com.carpooling.ums.exceptions;

public class PermissionServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PermissionServiceException(String message) {
        super(message);
    }

    public PermissionServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
