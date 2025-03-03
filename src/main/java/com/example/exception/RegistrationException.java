package com.example.exception;

public class RegistrationException extends Exception {
    public RegistrationException(String massage) {
        super(massage);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
