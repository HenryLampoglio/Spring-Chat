package com.example.SpringChat.infrastructure.user.adapter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserData user;

    @Getter @AllArgsConstructor
    public static class UserData {
        private String Email;
        private String nickname;
        private int publicIdentificationKey;
    }
}
