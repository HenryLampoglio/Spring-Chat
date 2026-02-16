package com.example.SpringChat.application.user.responseDTO;

import java.util.Optional;
import java.util.UUID;

public record UserDataResponseDTO(UUID id, String nickname, int publicIdentificationKey, String userQuote){

}