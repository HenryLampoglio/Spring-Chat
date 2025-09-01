package com.example.SpringChat.core.user.exception;

public class UserEmailNotFoundException extends RuntimeException {
    public UserEmailNotFoundException(String message) {
        super(message);
    }
}
