package com.example.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message, Exception e) {
        super(message);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
