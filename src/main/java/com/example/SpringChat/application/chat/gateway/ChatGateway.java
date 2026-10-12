package com.example.SpringChat.application.chat.gateway;

import com.example.SpringChat.core.chat.entity.Chat;
import com.example.SpringChat.core.chat.entity.UserChatLink;
import com.example.SpringChat.core.chat.entity.ChatMetadata;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatGateway {
    Optional<Chat> findPersonalChatByUsers(UUID userIdA, UUID userIdB);
    Chat createPersonalChat(UUID userIdA, UUID userIdB);
    Boolean verifyUserChatAccess(String userId, String chatId);
    String generateNewMessageUlid();
    
    List<UserChatLink> findUserChats(String userId);
    List<ChatMetadata> findChatsMetadata(List<String> chatIds);
    void updateLastMessage(String chatId, String content, String sentAt, String senderId);
}
