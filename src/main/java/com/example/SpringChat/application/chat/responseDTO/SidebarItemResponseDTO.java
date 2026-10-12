package com.example.SpringChat.application.chat.responseDTO;

public record SidebarItemResponseDTO(
        String chatId,
        String chatName,
        String role,
        String lastMessageContent,
        String lastMessageSentAt,
        String senderNickname
) {}
