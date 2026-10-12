package com.example.SpringChat.infrastructure.message.adapter.consumer;

import com.example.SpringChat.application.message.gateway.MessagePersistenceGateway;
import com.example.SpringChat.core.message.entity.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    private final MessagePersistenceGateway messagePersistenceGateway;

    public RabbitMQConsumer(MessagePersistenceGateway messagePersistenceGateway) {
        this.messagePersistenceGateway = messagePersistenceGateway;
    }

    @RabbitListener(queues = "fila.nosql")
    public void consumeMessages(Message message) {
        messagePersistenceGateway.saveMessage(message);
    }
}
