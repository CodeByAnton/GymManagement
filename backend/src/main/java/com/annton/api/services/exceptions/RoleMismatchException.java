package com.annton.api.services.exceptions;

public class RoleMismatchException extends RuntimeException {
    public RoleMismatchException(String message) {
        super(message);
    }
}
