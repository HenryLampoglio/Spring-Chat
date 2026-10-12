package com.example.SpringChat.application.message.gateway;

import com.example.SpringChat.core.message.entity.Message;

public interface MessagePersistenceGateway {
    void saveMessage(Message message);
    java.util.List<Message> findMessagesByChatId(java.util.UUID chatId, String beforeUlid, int limit);
}
