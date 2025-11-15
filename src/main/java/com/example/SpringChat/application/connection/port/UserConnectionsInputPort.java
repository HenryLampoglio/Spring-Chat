package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.UserConnectionCommand;
import com.example.SpringChat.core.connection.entity.Connection;

import java.util.List;

public interface UserConnectionsInputPort {
    List<Connection> execute(UserConnectionCommand userConnectionCommand);
}
