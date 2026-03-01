package com.example.SpringChat.application.chat.port;

import com.example.SpringChat.application.chat.command.FindOrCreatePersonalChatCommand;
import com.example.SpringChat.core.chat.entity.Chat;

public interface FindOrCreatePersonalChatInputPort {
    Chat execute(FindOrCreatePersonalChatCommand command);
}