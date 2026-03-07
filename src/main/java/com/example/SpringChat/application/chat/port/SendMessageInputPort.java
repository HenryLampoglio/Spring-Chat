package com.example.SpringChat.application.chat.port;

import com.example.SpringChat.application.chat.command.SendMessageCommand;

public interface SendMessageInputPort {
    void execute(SendMessageCommand command);
}
