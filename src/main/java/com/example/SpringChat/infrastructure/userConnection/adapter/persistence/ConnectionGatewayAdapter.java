package com.example.SpringChat.infrastructure.userConnection.adapter.persistence;

import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import com.example.SpringChat.infrastructure.user.persistence.repository.SpringUserRepository;
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
    public Optional<Connection> acceptInvite(UUID id,ConnectionStatus status)
    {
        Optional<ConnectionEntity> entity = this.springConnectionRepository.findByIdAndConnectionStatus(id,status);

        if(entity.isPresent()){
            entity.get().setConnectionStatus(ConnectionStatus.accepted);

            ConnectionEntity updatedEntity = this.springConnectionRepository.save(entity.get());
            return Optional.of(updatedEntity.toCoreConnection());
        }

        return Optional.empty();
    }

    @Override
    public PaginationResponse<Connection> searchUsers(UUID userId, PaginationRequest paginationRequest){

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
    public PaginationResponse<Connection> getInvitesSentByUser(UUID userId, PaginationRequest paginationRequest, ConnectionStatus status){
        Pageable springPageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());

        Page<ConnectionEntity> entityList = springConnectionRepository.findAllByRequesterIdAndConnectionStatusOrderByCreatedAt(userId, status, springPageable);

        List<Connection> sentInvitesConnectionCore = entityList.stream().map(ConnectionEntity::toCoreConnection).toList();

        return  new PaginationResponse<>(
                sentInvitesConnectionCore,
                entityList.getTotalElements(),
                entityList.getTotalPages()
        );
    }

    @Override
    public PaginationResponse<Connection> getInvitesReceivedByUser(UUID userId, PaginationRequest paginationRequest, ConnectionStatus status){
        Pageable springPageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());

        Page<ConnectionEntity> entityList = springConnectionRepository.findAllByReceiverIdAndConnectionStatusOrderByCreatedAt(userId, status, springPageable);

        List<Connection> sentInvitesConnectionCore = entityList.stream().map(ConnectionEntity::toCoreConnection).toList();

        return  new PaginationResponse<>(
                sentInvitesConnectionCore,
                entityList.getTotalElements(),
                entityList.getTotalPages()
        );
    }

    @Override
    public List<Connection> findAllByUserIdAndStatusWithUsers(UUID userId) {
        return List.of();
    }
}
