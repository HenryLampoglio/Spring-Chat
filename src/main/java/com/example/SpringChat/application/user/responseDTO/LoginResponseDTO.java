package com.example.SpringChat.application.user.responseDTO;

public record LoginResponseDTO(String token, String email, String nickname, int publicIdentificationKey) {
}
