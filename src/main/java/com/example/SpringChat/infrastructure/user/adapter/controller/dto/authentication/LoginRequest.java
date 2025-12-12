package com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    private String email;
    private String Password;
}
