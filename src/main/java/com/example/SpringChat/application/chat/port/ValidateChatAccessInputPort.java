package com.example.SpringChat.application.chat.port;

import com.example.SpringChat.application.chat.command.ValidateChatAccessCommand;

public interface ValidateChatAccessInputPort {
    void execute(ValidateChatAccessCommand command);
}
