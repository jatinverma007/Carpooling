package com.carpooling.ums.exceptions;

public class EmergencyContactServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmergencyContactServiceException(String message) {
        super(message);
    }

    public EmergencyContactServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
