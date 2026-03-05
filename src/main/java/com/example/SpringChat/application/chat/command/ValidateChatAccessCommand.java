package com.example.SpringChat.application.chat.command;

import java.util.UUID;

public record ValidateChatAccessCommand(String userId, String chatId){};
