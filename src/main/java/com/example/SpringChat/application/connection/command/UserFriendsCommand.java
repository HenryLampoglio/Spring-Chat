package com.example.SpringChat.application.connection.command;

import com.example.SpringChat.core.pagination.Pagination;

import java.util.UUID;

public record UserFriendsCommand(UUID userId, Pagination pagination) {
}
