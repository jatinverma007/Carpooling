package com.carpooling.ums.exceptions;

public class RolesPermissionActionServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RolesPermissionActionServiceException(String message) {
        super(message);
    }

    public RolesPermissionActionServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
