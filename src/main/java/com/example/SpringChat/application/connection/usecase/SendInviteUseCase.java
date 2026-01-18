package com.example.SpringChat.application.connection.usecase;

import com.example.SpringChat.application.connection.command.SendInviteCommand;
import com.example.SpringChat.application.connection.port.SendInvitePort;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.core.user.exception.UserNotFoundException;
import com.example.SpringChat.core.user.gateway.UserGateway;

public class SendInviteUseCase implements SendInvitePort {
    private final ConnectionGateway connectionGateway;
    private final UserGateway userGateway;


    public SendInviteUseCase(ConnectionGateway connectionGateway, UserGateway userGateway) {
        this.connectionGateway = connectionGateway;
        this.userGateway = userGateway;
    }

    @Override
    public Connection execute(SendInviteCommand command){
        if(!userGateway.userExists(command.receiverId())) throw new UserNotFoundException("Usuário não encontrado");

        Connection request = connectionGateway.sendInvite(command.requesterId(), command.receiverId(), ConnectionStatus.pending);
        return request;
    }
}
