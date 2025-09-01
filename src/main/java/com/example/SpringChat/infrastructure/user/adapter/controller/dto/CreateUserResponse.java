package com.example.SpringChat.infrastructure.user.adapter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter @AllArgsConstructor
public class CreateUserResponse {
    private UUID id;
    private String nickname;
    private String email;
    private int publicIdentificationKey;
}
