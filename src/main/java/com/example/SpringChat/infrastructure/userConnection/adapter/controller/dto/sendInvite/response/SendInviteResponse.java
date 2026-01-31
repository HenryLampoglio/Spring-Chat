package com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.sendInvite.response;

import com.example.SpringChat.core.connection.entity.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendInviteResponse {
    private String message;
    private Connection request;
}
