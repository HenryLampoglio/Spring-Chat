package com.example.SpringChat.application.user.responseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateUserResponseDTO(UUID id, String nickname, String email, int publicIdentificationKey, LocalDateTime createdAt) {
}
