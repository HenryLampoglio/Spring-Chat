package com.example.SpringChat.infrastructure.message.adapter.consumer;

import com.example.SpringChat.application.message.gateway.MessagePersistenceGateway;
import com.example.SpringChat.core.message.entity.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RabbitMQConsumerTest {

    @Mock
    private MessagePersistenceGateway messagePersistenceGateway;

    @InjectMocks
    private RabbitMQConsumer rabbitMQConsumer;

    @Test
    void shouldConsumeMessageAndSaveToGateway() {
        Message message = new Message(
                "01ARZ3NDEKTSV4RRFFQ69G5FAV",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "testUser",
                "Hello World",
                Instant.now()
        );

        rabbitMQConsumer.consumeMessages(message);

        Mockito.verify(messagePersistenceGateway, Mockito.times(1)).saveMessage(message);
    }
}
