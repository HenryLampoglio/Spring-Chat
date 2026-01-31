package com.example.SpringChat.application.connection.usecase;

import com.example.SpringChat.application.connection.command.CancelConnectionCommand;
import com.example.SpringChat.application.connection.port.CancelConnectionPort;
import com.example.SpringChat.core.connection.exception.ConnectionsNotFoundException;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;

public class CancelConnectionUseCase implements CancelConnectionPort {
    private final ConnectionGateway connectionGateway;

    public CancelConnectionUseCase(ConnectionGateway connectionGateway)
    {
        this.connectionGateway = connectionGateway;
    }


    @Override
    public void execute(CancelConnectionCommand command){
        if(!connectionGateway.connectionExists(command.id())) throw new ConnectionsNotFoundException("this connections doesn't exists");

        connectionGateway.cancelConnection(command.id());
    }
}
