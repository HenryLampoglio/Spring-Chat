package com.example.SpringChat.application.connection.responseDTO;

import java.time.LocalDateTime;

public record SendInviteResponseDTO(String connectionId, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
