package com.example.SpringChat.infrastructure.userConnection.adapter.persistence;

import com.example.SpringChat.application.shared.response.PaginationResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import com.example.SpringChat.infrastructure.user.persistence.repository.SpringUserRepository;
import com.example.SpringChat.infrastructure.userConnection.persistence.entity.ConnectionEntity;
import com.example.SpringChat.infrastructure.userConnection.persistence.repository.SpringConnectionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ConnectionGatewayAdapter implements ConnectionGateway {
    private final SpringConnectionRepository springConnectionRepository;
    private final SpringUserRepository springUserRepository;

    public ConnectionGatewayAdapter(SpringConnectionRepository springConnectionRepository, SpringUserRepository springUserRepository){
        this.springConnectionRepository = springConnectionRepository;
        this.springUserRepository = springUserRepository;
    }

    @Override
    public Boolean connectionExists(UUID id){
        return this.springConnectionRepository.existsById(id);
    }


    @Override
    public Connection sendInvite(UUID requesterId,UUID receiverId,ConnectionStatus status){
        ConnectionEntity rawEntity = new ConnectionEntity();

        rawEntity.setConnectionStatus(status);

        UserEntity requesterProxy = this.springUserRepository.getReferenceById(requesterId);
        rawEntity.setRequester(requesterProxy);

        UserEntity receiverProxy = this.springUserRepository.getReferenceById(receiverId);
        rawEntity.setReceiver(receiverProxy);

        ConnectionEntity entity = this.springConnectionRepository.save(rawEntity);

        return entity.toCoreConnection();
    }

    @Override
    public void cancelConnection(UUID id){
        this.springConnectionRepository.deleteById(id);
    }

    @Override
    public Optional<Connection> getInviteById(UUID connectionId, ConnectionStatus status){
        Optional<ConnectionEntity> entity = this.springConnectionRepository.findByIdAndConnectionStatus(connectionId,status);

        return entity.map(ConnectionEntity::toCoreConnection);

    }

    @Override
    public Connection acceptInvite(UUID id)
    {
        ConnectionEntity entity = this.springConnectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conexão não encontrada"));

        entity.setConnectionStatus(ConnectionStatus.ACCEPTED);

        return this.springConnectionRepository.save(entity).toCoreConnection();
    }

    @Override
    public PaginationResponseDTO<Connection> searchUsers(UUID userId, PaginationRequest paginationRequest){

        Pageable springPageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());

        Page<ConnectionEntity> entityList = springConnectionRepository.findAllByUserIdOrFriendIdWithUsers(ConnectionStatus.ACCEPTED, userId, springPageable);

        List<Connection> connectionsCore = entityList.stream().map(ConnectionEntity::toCoreConnection).toList();

        return new PaginationResponseDTO<>(
            connectionsCore,
            entityList.getNumber(),
            entityList.getTotalPages()
        );
    }

    @Override
    public PaginationResponseDTO<Connection> getInvitesSentByUser(UUID userId, PaginationRequest paginationRequest, ConnectionStatus status){
        Pageable springPageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());

        Page<ConnectionEntity> entityList = springConnectionRepository.findAllByRequesterIdAndConnectionStatusOrderByCreatedAt(userId, status, springPageable);

        List<Connection> sentInvitesConnectionCore = entityList.stream().map(ConnectionEntity::toCoreConnection).toList();

        return  new PaginationResponseDTO<>(
                sentInvitesConnectionCore,
                entityList.getNumber(),
                entityList.getTotalPages()
        );
    }

    @Override
    public PaginationResponseDTO<Connection> getInvitesReceivedByUser(UUID userId, PaginationRequest paginationRequest, ConnectionStatus status){
        Pageable springPageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());

        Page<ConnectionEntity> entityList = springConnectionRepository.findAllByReceiverIdAndConnectionStatusOrderByCreatedAt(userId, status, springPageable);

        List<Connection> sentInvitesConnectionCore = entityList.stream().map(ConnectionEntity::toCoreConnection).toList();

        return  new PaginationResponseDTO<>(
                sentInvitesConnectionCore,
                entityList.getNumber(),
                entityList.getTotalPages()
        );
    }

    @Override
    public List<Connection> findAllByUserIdAndStatusWithUsers(UUID userId) {
        return List.of();
    }
}
