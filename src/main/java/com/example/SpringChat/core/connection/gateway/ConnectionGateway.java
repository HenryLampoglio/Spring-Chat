package com.example.SpringChat.core.connection.gateway;

import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.application.shared.request.PaginationRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionGateway {

    Connection save(Connection connection);

    Optional<Connection> findById(UUID id);

    List<Connection> findAllByUserIdWithUsers(UUID userId);

    PaginationResponse<Connection> findAllByUserIdOrFriendIdWithUsers(UUID userId, PaginationRequest paginationRequest);

    List<Connection> findAllByUserIdAndStatusWithUsers(UUID userId);
}
