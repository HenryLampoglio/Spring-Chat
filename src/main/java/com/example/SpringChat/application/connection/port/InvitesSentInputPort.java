package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.InvitesSentCommand;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;

public interface InvitesSentInputPort {
    PaginationResponse<Connection> execute(InvitesSentCommand command);
}
