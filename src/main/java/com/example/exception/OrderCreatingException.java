package com.example.exception;

public class OrderCreatingException extends RuntimeException {
    public OrderCreatingException(String message) {
        super(message);
    }
}
