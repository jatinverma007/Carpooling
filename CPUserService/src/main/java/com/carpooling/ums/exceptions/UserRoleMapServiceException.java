package com.carpooling.ums.exceptions;

public class UserRoleMapServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserRoleMapServiceException(String message) {
        super(message);
    }

    public UserRoleMapServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
