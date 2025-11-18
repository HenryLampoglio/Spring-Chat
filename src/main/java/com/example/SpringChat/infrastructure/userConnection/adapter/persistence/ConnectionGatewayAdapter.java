package com.example.SpringChat.infrastructure.userConnection.adapter.persistence;

import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.pagination.Pagination;
import com.example.SpringChat.infrastructure.userConnection.persistence.entity.ConnectionEntity;
import com.example.SpringChat.infrastructure.userConnection.persistence.repository.SpringConnectionRepository;
import com.example.SpringChat.infrastructure.user.persistence.repository.SpringUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
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

    @Override
    public Optional<Connection> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Connection> findAllByUserIdWithUsers(UUID userId) {
        return List.of();
    }

    public List<Connection> findAllByUserIdOrFriendIdWithUsers(UUID userId, Pagination pagination){

        Pageable springPageable = PageRequest.of(pagination.getPage(), pagination.getSize());

        Page<ConnectionEntity> entityList = springConnectionRepository.findAllByUserIdOrFriendIdWithUsers(userId,springPageable);

        return entityList.stream().map(ConnectionEntity::toCoreConnection).toList();
    }

    @Override
    public List<Connection> findAllByUserIdAndStatusWithUsers(UUID userId) {
        return List.of();
    }
}
