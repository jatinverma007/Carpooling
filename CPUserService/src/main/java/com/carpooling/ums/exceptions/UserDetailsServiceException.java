package com.carpooling.ums.exceptions;

public class UserDetailsServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserDetailsServiceException(String message) {
        super(message);
    }

    public UserDetailsServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
