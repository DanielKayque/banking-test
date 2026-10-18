package com.api.banking_study.exceptions;

public class AuthenticatedUserRequiredException extends RuntimeException {
    public AuthenticatedUserRequiredException(String message) {
        super(message);
    }
}
