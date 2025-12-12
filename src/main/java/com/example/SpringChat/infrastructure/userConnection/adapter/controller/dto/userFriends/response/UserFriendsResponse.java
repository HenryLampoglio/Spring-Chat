package com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.userFriends.response;

import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
public class UserFriendsResponse {
    private String Message;
    private PaginationResponse<Connection> connections;
}
