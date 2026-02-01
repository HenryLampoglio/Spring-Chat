package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.SendInviteCommand;
import com.example.SpringChat.core.connection.entity.Connection;

public interface SendInvitePort {
    Connection execute(SendInviteCommand sendInviteCommand);
}
