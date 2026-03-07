package com.example.SpringChat.application.chat.usecase;

import com.example.SpringChat.application.chat.command.FindOrCreatePersonalChatCommand;
import com.example.SpringChat.application.chat.port.FindOrCreatePersonalChatInputPort;
import com.example.SpringChat.core.chat.entity.Chat;
import com.example.SpringChat.core.chat.gateway.ChatGateway;

public class FindOrCreatePersonalChatUseCase implements FindOrCreatePersonalChatInputPort {

    private final ChatGateway chatGateway;

    public FindOrCreatePersonalChatUseCase(ChatGateway chatGateway) {
        this.chatGateway = chatGateway;
    }

    @Override
    public Chat execute(FindOrCreatePersonalChatCommand command) {
        return chatGateway
                .findPersonalChatByUsers(command.authenticatedUserId(), command.targetUserId())
                .orElseGet(() -> chatGateway.createPersonalChat(command.authenticatedUserId(), command.targetUserId()));
    }
}