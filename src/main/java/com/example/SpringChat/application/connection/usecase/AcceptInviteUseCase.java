package com.example.SpringChat.application.connection.usecase;

import com.example.SpringChat.application.connection.command.AcceptInviteCommand;
import com.example.SpringChat.application.connection.port.AcceptInvitePort;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.exception.ConnectionsNotFoundException;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.enums.ConnectionStatus;

public class AcceptInviteUseCase implements AcceptInvitePort {
    private final ConnectionGateway connectionGateway;

    public AcceptInviteUseCase(ConnectionGateway connectionGateway) { this.connectionGateway = connectionGateway;}


    @Override
    public Connection execute(AcceptInviteCommand command){
        return connectionGateway.acceptInvite(command.id(), ConnectionStatus.pending)
                .orElseThrow(() -> new ConnectionsNotFoundException("this request doesn't exist or doesn't have the pending status"));
    }
}
