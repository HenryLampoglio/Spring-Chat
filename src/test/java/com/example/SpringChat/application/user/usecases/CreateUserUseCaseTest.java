// src/test/java/com/exemplo/SpringChat/application/user/usecase/CreateUserUseCaseTest.java

package com.example.SpringChat.application.user.usecases;

import com.example.SpringChat.application.user.command.CreateUserCommand;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.UserEmailAlreadyExistsException;
import com.example.SpringChat.application.user.gateway.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private CreateUserUseCase useCase;

    private CreateUserCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateUserCommand("testuser", "test@email.com", "password123");
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        Mockito.when(userGateway.existsByEmail(any())).thenReturn(false);
        Mockito.when(userGateway.existsByPublicIdentificationKey(anyInt())).thenReturn(false);
        Mockito.when(userGateway.encodePassword(any())).thenReturn("hashedPassword");
        Mockito.when(userGateway.save(any(User.class))).thenReturn(new User(
                "testuser", "test@email.com", "hashedPassword", 123456
        ));

        // Act
        User createdUser = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals("testuser", createdUser.getNickname());

        // Verify
        Mockito.verify(userGateway, Mockito.times(1)).existsByEmail(any());
        Mockito.verify(userGateway, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange (Configuração do cenário de falha)
        Mockito.when(userGateway.existsByEmail(any())).thenReturn(true);

        // Act & Assert (Verificamos se a exceção correta é lançada)
        Assertions.assertThrows(UserEmailAlreadyExistsException.class, () -> {
            useCase.execute(command);
        });

        // Verify
        Mockito.verify(userGateway, Mockito.never()).save(any(User.class));
    }
}