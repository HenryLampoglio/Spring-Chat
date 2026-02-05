package com.example.SpringChat.application.connection.responseDTO;

import com.example.SpringChat.core.enums.ConnectionStatus;

import java.util.UUID;

public record InvitesSentResponseDTO(UUID connectionId, String receiverNickname, int receiverPublicIdentificationKey, ConnectionStatus connectionStatus) {
}
