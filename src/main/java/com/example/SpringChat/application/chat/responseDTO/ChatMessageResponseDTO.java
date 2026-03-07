package com.example.SpringChat.application.chat.responseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageResponseDTO(
        UUID chatId,
        String senderId,
        String senderNickname,
        String content,
        String createdAt
) {}
