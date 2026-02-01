package com.example.SpringChat.application.user.usecases;

import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.PasswordsDoesntMatchesException;
import com.example.SpringChat.core.user.exception.UserEmailNotFoundException;
import com.example.SpringChat.core.user.gateway.UserGateway;
import com.example.SpringChat.infrastructure.security.TokenService;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

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
        Mockito.when(passwordEncoder.matches(command.password(), foundUser.getHashedPassword()))
                .thenReturn(true);
        Mockito.when(tokenService.generateToken(foundUser))
                .thenReturn("generated-jwt-token");

        LoginResponse userAuth = useCase.execute(command);

        Assertions.assertNotNull(userAuth);
        Assertions.assertEquals("generated-jwt-token", userAuth.getToken());

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

        Mockito.when(passwordEncoder.matches(command.password(), foundUser.getHashedPassword()))
                .thenReturn(false);

        Assertions.assertThrows(PasswordsDoesntMatchesException.class, () -> {
            useCase.execute(command);
        });
    }
}
