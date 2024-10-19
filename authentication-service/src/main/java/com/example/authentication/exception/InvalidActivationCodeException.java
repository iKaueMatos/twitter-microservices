package com.example.authentication.exception;

public class InvalidActivationCodeException extends RuntimeException {
    public InvalidActivationCodeException(String message) {
        super(message);
    }
}
