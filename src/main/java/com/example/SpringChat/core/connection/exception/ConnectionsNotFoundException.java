package com.example.SpringChat.core.connection.exception;

public class ConnectionsNotFoundException extends RuntimeException {
    public ConnectionsNotFoundException(String message) {
        super(message);
    }
}
