package com.example.SpringChat.application.connection.port;

import com.example.SpringChat.application.connection.command.CancelConnectionCommand;

public interface CancelConnectionPort {
    void execute(CancelConnectionCommand cancelConnectionCommand);
}
