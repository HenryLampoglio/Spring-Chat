package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.UserFriendsCommand;
import com.example.SpringChat.core.connection.entity.Connection;

import java.util.List;

public interface UserFriendsInputPort {
    List<Connection> execute(UserFriendsCommand userFriendsCommand);
}
