package com.example.SpringChat.application.connection.responseDTO;

import com.example.SpringChat.application.user.responseDTO.UserDataResponseDTO;
import com.example.SpringChat.core.enums.ConnectionStatus;

import java.util.UUID;

public record UserFriendsResponseDTO(UUID connectionId, ConnectionStatus connectionStatus, UserDataResponseDTO receiverData) {
}
