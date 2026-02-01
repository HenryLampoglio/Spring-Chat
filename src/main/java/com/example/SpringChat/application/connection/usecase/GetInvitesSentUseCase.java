package com.example.SpringChat.application.connection.usecase;

import com.example.SpringChat.application.connection.command.InvitesSentCommand;
import com.example.SpringChat.application.connection.port.InvitesSentInputPort;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.exception.InvitesSolicitationsNotFoundException;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.enums.ConnectionStatus;

public class GetInvitesSentUseCase implements InvitesSentInputPort {
    private final ConnectionGateway connectionGateway;

    public  GetInvitesSentUseCase(ConnectionGateway connectionGateway) {this.connectionGateway = connectionGateway;}

    @Override
    public PaginationResponse<Connection> execute(InvitesSentCommand command){
        PaginationRequest paginationRequest = command.paginationRequest();

        PaginationResponse<Connection> pendingSentConnections = connectionGateway.getInvitesSentByUser(command.userId(), paginationRequest, ConnectionStatus.pending);

        if(pendingSentConnections.getItems().isEmpty()){
            throw new InvitesSolicitationsNotFoundException("Não existe nenhum convite pendente enviado por você");
        }

        return  pendingSentConnections;
    }
}
