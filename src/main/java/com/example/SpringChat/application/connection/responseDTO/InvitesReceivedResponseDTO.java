package com.example.SpringChat.application.connection.responseDTO;

import com.example.SpringChat.core.enums.ConnectionStatus;

import java.util.UUID;

public record InvitesReceivedResponseDTO(UUID connectionId, String requesterNickname, int requesterPublicIdentificationKey, ConnectionStatus connectionStatus) {
}
