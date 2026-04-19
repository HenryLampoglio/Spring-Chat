package com.example.SpringChat.application.chat.command;

import java.util.UUID;

public record SendMessageCommand(UUID chatId, UUID senderId,String senderNickname, String message) {
}
