package com.api.banking_study.exceptions;

public class InvalidDestinationAccountException extends RuntimeException {
    public InvalidDestinationAccountException(String message) {
        super(message);
    }
}
