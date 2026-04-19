package com.example.SpringChat.application.message.gateway;

import com.example.SpringChat.core.message.entity.Message;

public interface MessagePersistenceGateway {
    void saveMessage(Message message);
}
