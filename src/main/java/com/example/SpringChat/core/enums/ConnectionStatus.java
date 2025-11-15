package com.example.SpringChat.core.enums;

import com.example.SpringChat.core.connection.entity.Connection;
import lombok.Getter;

@Getter
public enum ConnectionStatus {
    ACCEPTED("accepted"),
    PENDING("pending"),
    BLOCKED("blocked");

    private final String value;

    ConnectionStatus(String value){
        this.value = value;
    }

}
