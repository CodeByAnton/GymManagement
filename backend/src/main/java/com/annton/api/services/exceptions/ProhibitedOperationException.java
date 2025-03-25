package com.annton.api.services.exceptions;

public class ProhibitedOperationException extends RuntimeException {
    public ProhibitedOperationException(String message) {
        super(message);
    }
}
