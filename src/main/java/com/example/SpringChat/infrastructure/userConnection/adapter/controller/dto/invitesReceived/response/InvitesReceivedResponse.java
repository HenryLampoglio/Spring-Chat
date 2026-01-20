package com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.invitesReceived.response;

import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvitesReceivedResponse {
    private PaginationResponse<Connection> connections;
}
