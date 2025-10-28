package com.example.SpringChat.infrastructure.userConnection.adapter.persistence;

import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.infrastructure.userConnection.persistence.entity.ConnectionEntity;
import com.example.SpringChat.infrastructure.userConnection.persistence.repository.SpringConnectionRepository;
import com.example.SpringChat.infrastructure.user.persistence.repository.SpringUserRepository;

import java.util.List;
import java.util.UUID;

public class ConnectionGatewayAdapter implements ConnectionGateway {
    private final SpringConnectionRepository springConnectionRepository;

    public ConnectionGatewayAdapter(SpringConnectionRepository springConnectionRepository){
        this.springConnectionRepository = springConnectionRepository;
    }

    public Connection save(Connection connection){
        ConnectionEntity connectionEntity = new ConnectionEntity(connection);
        ConnectionEntity savedEntity = springConnectionRepository.save(connectionEntity);
        return savedEntity.toCoreConnection();
    }

    // finalizar isso na branch de casa
    public List<Connection> findAllByFriendIdWithUsers(UUID userId){
        return springConnectionRepository.findAllByFriendIdWithUsers(userId);
    }
}
