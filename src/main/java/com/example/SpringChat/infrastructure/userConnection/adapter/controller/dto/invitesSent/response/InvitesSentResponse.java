package com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.invitesSent.response;

import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvitesSentResponse {
    private PaginationResponse<Connection> connections;
}
