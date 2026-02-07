package com.example.SpringChat.core.connection.gateway;

import com.example.SpringChat.application.shared.response.PaginationResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.core.enums.ConnectionStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionGateway {

    Boolean connectionExists(UUID id);

    Connection sendInvite(UUID requesterId, UUID receiverId, ConnectionStatus status);



    void cancelConnection(UUID connectionId);

    Optional<Connection> acceptInvite(UUID connectionId, ConnectionStatus status);

    PaginationResponseDTO<Connection> searchUsers(UUID userId, PaginationRequest paginationRequest);

    PaginationResponseDTO<Connection> getInvitesSentByUser(UUID userId, PaginationRequest paginationRequest, ConnectionStatus status);

    PaginationResponseDTO<Connection> getInvitesReceivedByUser(UUID userId, PaginationRequest paginationRequest, ConnectionStatus status);

    List<Connection> findAllByUserIdAndStatusWithUsers(UUID userId);
}
