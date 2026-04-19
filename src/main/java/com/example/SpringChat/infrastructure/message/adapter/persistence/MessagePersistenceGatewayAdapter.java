package com.example.SpringChat.infrastructure.message.adapter.persistence;

import com.example.SpringChat.application.message.gateway.MessagePersistenceGateway;
import com.example.SpringChat.core.message.entity.Message;
import com.example.SpringChat.infrastructure.message.persistence.repository.DynamoMessageRepository;
import org.springframework.stereotype.Component;

@Component
public class MessagePersistenceGatewayAdapter implements MessagePersistenceGateway {
    private final DynamoMessageRepository dynamoMessageRepository;

    public  MessagePersistenceGatewayAdapter(DynamoMessageRepository dynamoMessageRepository){
        this.dynamoMessageRepository = dynamoMessageRepository;
    }

    @Override
    public void saveMessage(Message message) {
        dynamoMessageRepository.save(message);
    }
}
