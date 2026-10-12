package com.example.SpringChat.application.message.usecases;

import com.example.SpringChat.application.message.command.SaveMessageCommand;
import com.example.SpringChat.application.message.gateway.MessagePersistenceGateway;
import com.example.SpringChat.application.message.usecase.SaveMessageUseCase;
import com.example.SpringChat.core.message.entity.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class SaveMessageUseCaseTest {

    @Mock
    private MessagePersistenceGateway messagePersistenceGateway;

    @InjectMocks
    private SaveMessageUseCase useCase;

    @Test
    void shouldSaveMessageSuccessfully() {
        Message message = new Message(
                "01ARZ3NDEKTSV4RRFFQ69G5FAV",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "nickname",
                "Hello World",
                Instant.now()
        );

        SaveMessageCommand command = new SaveMessageCommand(message);

        // Act
        useCase.execute(command);

        // Assert
        Mockito.verify(messagePersistenceGateway, Mockito.times(1)).saveMessage(eq(message));
    }
}
