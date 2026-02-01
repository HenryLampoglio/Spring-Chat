package com.example.SpringChat.application.connection.command;

import java.util.UUID;

public record SendInviteCommand(UUID requesterId, UUID receiverId) {
}
