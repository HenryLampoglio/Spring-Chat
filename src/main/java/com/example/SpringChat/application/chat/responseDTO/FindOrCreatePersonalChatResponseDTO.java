package com.example.SpringChat.application.chat.responseDTO;

import com.example.SpringChat.core.enums.ChatType;

import java.time.LocalDateTime;
import java.util.UUID;

public record FindOrCreatePersonalChatResponseDTO(
        UUID id,
        LocalDateTime createdAt
) {}