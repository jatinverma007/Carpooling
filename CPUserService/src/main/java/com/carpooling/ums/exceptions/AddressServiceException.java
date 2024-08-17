package com.carpooling.ums.exceptions;


public class AddressServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AddressServiceException(String message) {
        super(message);
    }

    public AddressServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
