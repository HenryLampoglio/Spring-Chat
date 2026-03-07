package com.example.SpringChat.application.chat.command;

import java.util.UUID;

public record FindOrCreatePersonalChatCommand(UUID authenticatedUserId, UUID targetUserId) {
}