package com.example.SpringChat.core.connection.gateway;

import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.enums.ConnectionStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionGateway {

    Connection save(Connection connection);

    Optional<Connection> findById(UUID id);

    List<Connection> findAllByUserIdWithUsers(UUID userId);

    List<Connection> findAllByFriendIdWithUsers(UUID userId);

    List<Connection> findAllByUserIdAndStatusWithUsers(UUID userId);
}
