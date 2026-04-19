package com.example.SpringChat.application.message.port;

import com.example.SpringChat.application.message.command.SaveMessageCommand;

public interface SaveMessageInputPort {
    void execute(SaveMessageCommand saveMessageCommand);
}
