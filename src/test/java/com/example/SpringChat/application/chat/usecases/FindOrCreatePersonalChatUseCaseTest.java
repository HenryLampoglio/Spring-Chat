package com.example.SpringChat.application.chat.usecases;

import com.example.SpringChat.application.chat.command.FindOrCreatePersonalChatCommand;
import com.example.SpringChat.application.chat.usecase.FindOrCreatePersonalChatUseCase;
import com.example.SpringChat.core.chat.entity.Chat;
import com.example.SpringChat.core.chat.gateway.ChatGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class FindOrCreatePersonalChatUseCaseTest {

    @Mock
    ChatGateway chatGateway;

    @InjectMocks
    private FindOrCreatePersonalChatUseCase useCase;

    @Test
    void shouldReturnExistingChatWhenItAlreadyExists() {
        UUID authenticatedUserId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        Chat existingChat = new Chat();

        // arrange
        Mockito.when(chatGateway.findPersonalChatByUsers(eq(authenticatedUserId), eq(targetUserId)))
                .thenReturn(Optional.of(existingChat));

        // act
        FindOrCreatePersonalChatCommand command = new FindOrCreatePersonalChatCommand(authenticatedUserId, targetUserId);
        Chat response = useCase.execute(command);

        // assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(existingChat, response);
        Mockito.verify(chatGateway, Mockito.times(1)).findPersonalChatByUsers(eq(authenticatedUserId), eq(targetUserId));
        Mockito.verify(chatGateway, Mockito.never()).createPersonalChat(any(), any());
    }

    @Test
    void shouldCreateChatWhenItDoesNotExist() {
        UUID authenticatedUserId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        Chat newChat = new Chat();

        // arrange
        Mockito.when(chatGateway.findPersonalChatByUsers(eq(authenticatedUserId), eq(targetUserId)))
                .thenReturn(Optional.empty());
        Mockito.when(chatGateway.createPersonalChat(eq(authenticatedUserId), eq(targetUserId)))
                .thenReturn(newChat);

        // act
        FindOrCreatePersonalChatCommand command = new FindOrCreatePersonalChatCommand(authenticatedUserId, targetUserId);
        Chat response = useCase.execute(command);

        // assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(newChat, response);
        Mockito.verify(chatGateway, Mockito.times(1)).findPersonalChatByUsers(eq(authenticatedUserId), eq(targetUserId));
        Mockito.verify(chatGateway, Mockito.times(1)).createPersonalChat(eq(authenticatedUserId), eq(targetUserId));
    }
}