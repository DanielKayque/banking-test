package com.api.banking_study.exceptions;

public class DestinationAccountNotFoundException extends RuntimeException {
    public DestinationAccountNotFoundException(String message) {
        super(message);
    }
}
