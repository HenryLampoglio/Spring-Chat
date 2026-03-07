package com.example.SpringChat.core.chat.gateway;

import com.example.SpringChat.core.chat.entity.Chat;

import java.util.Optional;
import java.util.UUID;

public interface ChatGateway {
    Optional<Chat> findPersonalChatByUsers(UUID userIdA, UUID userIdB);
    Chat createPersonalChat(UUID userIdA, UUID userIdB);
    Boolean verifyUserChatAccess(String userId, String chatId);
}
