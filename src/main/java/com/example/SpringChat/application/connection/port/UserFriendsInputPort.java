package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.UserFriendsCommand;
import com.example.SpringChat.application.shared.response.PaginationResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;

public interface UserFriendsInputPort {
    PaginationResponseDTO<Connection> execute(UserFriendsCommand userFriendsCommand);
}
