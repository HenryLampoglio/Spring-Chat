package com.example.SpringChat.application.chat.command;

import java.util.UUID;

public record ValidateChatAccessCommand(String userEmail, String chatId){};
