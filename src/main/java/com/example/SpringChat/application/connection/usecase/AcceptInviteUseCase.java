package com.example.SpringChat.application.connection.usecase;

import com.example.SpringChat.application.connection.command.AcceptInviteCommand;
import com.example.SpringChat.application.connection.port.AcceptInvitePort;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.exception.ConnectionsNotFoundException;
import com.example.SpringChat.core.connection.exception.ForbiddenAcceptInviteException;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.enums.ConnectionStatus;

public class AcceptInviteUseCase implements AcceptInvitePort {
    private final ConnectionGateway connectionGateway;

    public AcceptInviteUseCase(ConnectionGateway connectionGateway) { this.connectionGateway = connectionGateway;}


    @Override
    public Connection execute(AcceptInviteCommand command){
        Connection connection = connectionGateway.getInviteById(command.id(), ConnectionStatus.PENDING)
                .orElseThrow(() -> new ConnectionsNotFoundException("this request doesn't exist or doesn't have the pending status"));

        if(!connection.getReceiver().getId().equals(command.userId())) throw new ForbiddenAcceptInviteException("Você só pode aceitar convites que foram enviados para você");

        return connectionGateway.acceptInvite(command.id());
    }
}
