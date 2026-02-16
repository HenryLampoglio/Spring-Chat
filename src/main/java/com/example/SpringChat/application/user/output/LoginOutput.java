package com.example.SpringChat.application.user.output;

import com.example.SpringChat.core.user.entity.User;

public record LoginOutput(String token, User user) {
}
