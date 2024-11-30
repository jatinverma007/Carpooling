package com.carpooling.ums.exceptions;

public class UsersTncServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsersTncServiceException(String message) {
        super(message);
    }

    public UsersTncServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
