package com.example.SpringChat.application.user.usecase;

import com.example.SpringChat.application.user.command.SearchUsersCommand;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.gateway.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class SearchUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private SearchUsersUseCase useCase;

    private SearchUsersCommand command;

    @BeforeEach
    void setUp(){
        command = new SearchUsersCommand("test#123", UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

    @Test
    void shouldReturnUserListSuccessfully(){
        Mockito.when(userGateway.findTop10NicknameContainingAndPublicIdentificationKeyContaining(any(),anyInt(),any()))
                        .thenReturn(List.of(new User()));

        List<User> userList = useCase.execute(command);

        Assertions.assertNotNull(userList);

        Mockito.verify(userGateway,Mockito.times(1)).findTop10NicknameContainingAndPublicIdentificationKeyContaining(any(),anyInt(),any());
    }
}
