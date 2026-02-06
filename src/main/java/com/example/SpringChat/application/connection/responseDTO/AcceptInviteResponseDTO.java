package com.example.SpringChat.application.connection.responseDTO;

import com.example.SpringChat.core.enums.ConnectionStatus;

import java.time.LocalDateTime;

public record AcceptInviteResponseDTO(String connectionId, LocalDateTime createdAt, LocalDateTime updatedAt, ConnectionStatus connectionStatus) {
}
