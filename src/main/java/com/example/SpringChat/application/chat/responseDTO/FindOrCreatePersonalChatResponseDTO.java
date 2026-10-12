package com.example.SpringChat.application.chat.responseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta contendo o ID do chat direto")
public record FindOrCreatePersonalChatResponseDTO(
        @Schema(description = "UUID do chat", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
        UUID id,

        @Schema(description = "Data e hora de criação do chat", example = "2026-10-12T17:30:00")
        LocalDateTime createdAt
) {
    public FindOrCreatePersonalChatResponseDTO(UUID id) {
        this(id, LocalDateTime.now());
    }
}