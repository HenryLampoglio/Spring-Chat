package com.example.SpringChat.core.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PasswordsDoesntMatchesException extends RuntimeException {
    public PasswordsDoesntMatchesException(String message) {
        super(message);
    }
}
