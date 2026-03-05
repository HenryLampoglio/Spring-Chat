package com.example.SpringChat.application.chat.command;

import java.util.UUID;

public record SendMessageCommand(UUID chatId, String senderId,String senderNickname, String message) {
}
