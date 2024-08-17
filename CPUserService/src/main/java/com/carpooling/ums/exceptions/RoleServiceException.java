package com.carpooling.ums.exceptions;

public class RoleServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RoleServiceException(String message) {
        super(message);
    }

    public RoleServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

