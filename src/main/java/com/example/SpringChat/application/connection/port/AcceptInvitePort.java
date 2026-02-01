package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.AcceptInviteCommand;
import com.example.SpringChat.core.connection.entity.Connection;

public interface AcceptInvitePort {
    Connection execute(AcceptInviteCommand command);
}
