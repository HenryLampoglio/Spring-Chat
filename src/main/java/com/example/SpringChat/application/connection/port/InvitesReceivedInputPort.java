package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.InvitesReceivedCommand;
import com.example.SpringChat.application.shared.response.PaginationResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;

public interface InvitesReceivedInputPort {
    PaginationResponseDTO<Connection> execute(InvitesReceivedCommand command);
}
