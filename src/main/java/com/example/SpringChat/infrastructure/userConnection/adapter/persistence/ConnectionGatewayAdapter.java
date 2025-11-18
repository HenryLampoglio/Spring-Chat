package com.example.SpringChat.infrastructure.userConnection.adapter.persistence;

import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.infrastructure.userConnection.persistence.entity.ConnectionEntity;
import com.example.SpringChat.infrastructure.userConnection.persistence.repository.SpringConnectionRepository;
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

    public PaginationResponse<Connection> findAllByUserIdOrFriendIdWithUsers(UUID userId, PaginationRequest paginationRequest){

        Pageable springPageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());

        Page<ConnectionEntity> entityList = springConnectionRepository.findAllByUserIdOrFriendIdWithUsers(ConnectionStatus.accepted, userId, springPageable);

        List<Connection> connectionsCore = entityList.stream().map(ConnectionEntity::toCoreConnection).toList();

        return new PaginationResponse<>(
            connectionsCore,
            entityList.getTotalElements(),
            entityList.getTotalPages()
        );
    }

    @Override
    public List<Connection> findAllByUserIdAndStatusWithUsers(UUID userId) {
        return List.of();
    }
}
