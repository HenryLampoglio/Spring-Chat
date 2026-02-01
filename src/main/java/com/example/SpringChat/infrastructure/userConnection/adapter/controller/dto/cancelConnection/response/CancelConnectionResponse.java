package com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.cancelConnection.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CancelConnectionResponse {
    private String message;
    private Boolean success;
}
