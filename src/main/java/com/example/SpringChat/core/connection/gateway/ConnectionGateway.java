package com.example.SpringChat.core.connection.gateway;

import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.core.enums.ConnectionStatus;

import java.util.List;
import java.util.UUID;

public interface ConnectionGateway {

    Boolean connectionExists(UUID id);

    Connection sendInvite(UUID requesterId, UUID receiverId, ConnectionStatus status);

    void cancelConnection(UUID connectionId);

    Connection acceptInvite(UUID connectionId);

    Void refuseInvite(UUID connectionId);

    PaginationResponse<Connection> searchUsers(UUID userId, PaginationRequest paginationRequest);

    List<Connection> findAllByUserIdAndStatusWithUsers(UUID userId);
}
