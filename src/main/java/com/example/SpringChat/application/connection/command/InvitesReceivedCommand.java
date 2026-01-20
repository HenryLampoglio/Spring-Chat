package com.example.SpringChat.application.connection.command;

import com.example.SpringChat.application.shared.request.PaginationRequest;

import java.util.UUID;

public record InvitesReceivedCommand(UUID userId, PaginationRequest paginationRequest) {
}
