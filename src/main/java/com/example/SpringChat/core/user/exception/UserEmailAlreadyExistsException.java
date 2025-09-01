package com.example.SpringChat.core.user.exception;

public class UserEmailAlreadyExistsException extends RuntimeException {

    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }
}
