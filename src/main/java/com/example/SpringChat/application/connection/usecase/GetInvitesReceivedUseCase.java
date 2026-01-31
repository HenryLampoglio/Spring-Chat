package com.example.SpringChat.application.connection.usecase;

import com.example.SpringChat.application.connection.command.InvitesReceivedCommand;
import com.example.SpringChat.application.connection.port.InvitesReceivedInputPort;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.exception.InvitesSolicitationsNotFoundException;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.enums.ConnectionStatus;

public class GetInvitesReceivedUseCase implements InvitesReceivedInputPort {
    private final ConnectionGateway connectionGateway;

    public  GetInvitesReceivedUseCase(ConnectionGateway connectionGateway) {this.connectionGateway = connectionGateway;}

    @Override
    public PaginationResponse<Connection> execute(InvitesReceivedCommand command){
        PaginationRequest paginationRequest = command.paginationRequest();

        PaginationResponse<Connection> pendingReceivedConnections = connectionGateway.getInvitesReceivedByUser(command.userId(), paginationRequest, ConnectionStatus.pending);

        if(pendingReceivedConnections.getItems().isEmpty()){
            throw new InvitesSolicitationsNotFoundException("Não existe nenhum convite pendente para você");
        }

        return  pendingReceivedConnections;
    }
}
