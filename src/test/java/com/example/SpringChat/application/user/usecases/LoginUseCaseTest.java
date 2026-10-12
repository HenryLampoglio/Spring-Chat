package com.example.SpringChat.application.user.usecases;

import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.application.user.output.LoginOutput;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.PasswordsDoesntMatchesException;
import com.example.SpringChat.core.user.exception.UserEmailNotFoundException;
import com.example.SpringChat.application.user.gateway.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private LoginUseCase useCase;

    private LoginCommand command;
    private User foundUser;

    @BeforeEach
    void setUp() {
        String raw_password = "raw_password_from_command";
        String hashed_password = "hashed_password_from_database";
        command = new LoginCommand("test@mail.com", raw_password);
        foundUser = new User("test", "test@mail.com", hashed_password, 123);
    }

    @Test
    void shouldReturnTokenSuccessfully() {
        Mockito.when(userGateway.findByEmail(command.Email()))
                .thenReturn(Optional.of(foundUser));
        Mockito.when(userGateway.validatePassword(command.password(), foundUser.getHashedPassword()))
                .thenReturn(true);
        Mockito.when(userGateway.generateAuthToken(foundUser))
                .thenReturn("generated-jwt-token");

        LoginOutput userAuth = useCase.execute(command);

        Assertions.assertNotNull(userAuth);
        Assertions.assertEquals("generated-jwt-token", userAuth.token());

        Mockito.verify(userGateway, Mockito.times(1)).findByEmail(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNotFound() {
        Mockito.when(userGateway.findByEmail(command.Email()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserEmailNotFoundException.class, () ->{
            useCase.execute(command);
        });
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch(){
        Mockito.when(userGateway.findByEmail(command.Email()))
                .thenReturn(Optional.of(foundUser));

        Mockito.when(userGateway.validatePassword(command.password(), foundUser.getHashedPassword()))
                .thenReturn(false);

        Assertions.assertThrows(PasswordsDoesntMatchesException.class, () -> {
            useCase.execute(command);
        });
    }
}
