package com.example.SpringChat.core.connection.gateway;

import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.pagination.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionGateway {

    Connection save(Connection connection);

    Optional<Connection> findById(UUID id);

    List<Connection> findAllByUserIdWithUsers(UUID userId);

    List<Connection> findAllByUserIdOrFriendIdWithUsers(UUID userId, Pagination pagination);

    List<Connection> findAllByUserIdAndStatusWithUsers(UUID userId);
}
