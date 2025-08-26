package com.example.SpringChat.application.user.command;

import java.util.Optional;

public record CreateUserCommand(String nickname, String email, String password, int publicIdentificationKey, Optional<String> userQuote) {
}
