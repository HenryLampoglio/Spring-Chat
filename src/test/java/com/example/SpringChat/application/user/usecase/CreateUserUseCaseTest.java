// src/test/java/com/exemplo/SpringChat/application/user/usecase/CreateUserUseCaseTest.java

package com.example.SpringChat.application.user.usecase;

import com.example.SpringChat.application.user.command.CreateUserCommand;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.UserEmailAlreadyExistsException;
import com.example.SpringChat.core.user.gateway.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase useCase;

    private CreateUserCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateUserCommand("testuser", "test@email.com", "password123");
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange (Configuração do cenário)
        // Dizemos ao Mockito o que o mock deve fazer quando os métodos são chamados
        Mockito.when(userGateway.existsByEmail(any())).thenReturn(false);
        Mockito.when(userGateway.existsByPublicIdentificationKey(anyInt())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        Mockito.when(userGateway.save(any(User.class))).thenReturn(new User(
                "testuser", "test@email.com", "hashedPassword", 123456
        ));

        // Act (Execução do código)
        User createdUser = useCase.execute(command);

        // Assert (Verificação dos resultados)
        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals("testuser", createdUser.getNickname());

        // Verificamos se os métodos do mock foram chamados o número de vezes correto
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

        // Verificamos que o método save() nunca foi chamado neste caso
        Mockito.verify(userGateway, Mockito.never()).save(any(User.class));
    }
}