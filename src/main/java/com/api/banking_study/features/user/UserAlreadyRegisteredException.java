package com.api.banking_study.features.user;

public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException(String field) {
        super("User already registered with this " + field);
    }
}
