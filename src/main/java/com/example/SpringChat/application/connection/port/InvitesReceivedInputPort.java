package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.InvitesReceivedCommand;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;

public interface InvitesReceivedInputPort {
    PaginationResponse<Connection> execute(InvitesReceivedCommand command);
}
