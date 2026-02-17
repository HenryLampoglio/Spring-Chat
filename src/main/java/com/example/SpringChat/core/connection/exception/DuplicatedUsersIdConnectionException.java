package com.example.SpringChat.core.connection.exception;

public class DuplicatedUsersIdConnectionException extends RuntimeException {
    public DuplicatedUsersIdConnectionException(String message) {
        super(message);
    }
}
