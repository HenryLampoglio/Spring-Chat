package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.InvitesSentCommand;
import com.example.SpringChat.application.shared.response.PaginationResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;

public interface InvitesSentInputPort {
    PaginationResponseDTO<Connection> execute(InvitesSentCommand command);
}
