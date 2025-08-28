package com.example.SpringChat.infrastructure.user.adapter.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateUserRequest {
    private String nickname;
    private String email;
    private String hashedPassword;
}
