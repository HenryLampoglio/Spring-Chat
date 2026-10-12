package com.example.SpringChat.application.chat.responseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Dados de uma mensagem de chat")
public record ChatMessageResponseDTO(
        @Schema(description = "Identificador único da mensagem (ULID ordenável)", example = "01ARZ3NDEKTSV4RRFFQ69G5FAV")
        String id,

        @Schema(description = "UUID do chat ao qual a mensagem pertence", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
        UUID chatId,

        @Schema(description = "UUID do usuário remetente", example = "b2c3d4e5-f6a7-8901-bcde-f12345678901")
        UUID senderId,

        @Schema(description = "Apelido do usuário remetente", example = "joaodev")
        String senderNickname,

        @Schema(description = "Conteúdo textual da mensagem", example = "Olá, tudo bem?")
        String content,

        @Schema(description = "Timestamp ISO-8601 do envio da mensagem", example = "2026-10-12T17:30:00Z")
        String createdAt
) {
    public ChatMessageResponseDTO(UUID chatId, UUID senderId, String senderNickname, String content, String createdAt) {
        this(null, chatId, senderId, senderNickname, content, createdAt);
    }
}
