package com.example.SpringChat.application.connection.usecase;

import com.example.SpringChat.application.connection.command.UserFriendsCommand;
import com.example.SpringChat.application.connection.port.UserFriendsInputPort;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.exception.ConnectionsNotFoundException;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.pagination.Pagination;

import java.util.List;
import java.util.UUID;

public class SearchUserFriendsUseCase implements UserFriendsInputPort {
    private final ConnectionGateway connectionGateway;

    public SearchUserFriendsUseCase(ConnectionGateway connectionGateway) {this.connectionGateway = connectionGateway;}

    @Override
    public List<Connection> execute(UserFriendsCommand command){
        UUID userId = command.userId();
        Pagination pagination = command.pagination();

        List<Connection> connections = connectionGateway.findAllByUserIdOrFriendIdWithUsers(userId, pagination);

        if(connections.isEmpty()){
            throw new ConnectionsNotFoundException("This user didn't make any connections yet");
        }

        return connections;
    }
}
