package com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.acceptInvite.response;

import com.example.SpringChat.core.connection.entity.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcceptInviteResponse {
    private Connection connection;
    private String message;
}
