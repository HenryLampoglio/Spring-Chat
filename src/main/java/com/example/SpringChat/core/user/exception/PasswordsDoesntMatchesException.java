package com.example.SpringChat.core.user.exception;

public class PasswordsDoesntMatchesException extends RuntimeException {
    public PasswordsDoesntMatchesException(String message) {
        super(message);
    }
}
