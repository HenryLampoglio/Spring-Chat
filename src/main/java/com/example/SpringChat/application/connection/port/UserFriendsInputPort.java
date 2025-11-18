package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.UserFriendsCommand;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;

import java.util.List;

public interface UserFriendsInputPort {
    PaginationResponse<Connection> execute(UserFriendsCommand userFriendsCommand);
}
