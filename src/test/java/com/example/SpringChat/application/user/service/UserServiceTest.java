package com.example.SpringChat.application.user.service;

import com.example.SpringChat.application.user.gateway.UserGateway;
import com.example.SpringChat.application.user.responseDTO.CreateUserResponseDTO;
import com.example.SpringChat.application.user.responseDTO.LoginResponseDTO;
import com.example.SpringChat.application.user.responseDTO.SearchUserResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.PasswordsDoesntMatchesException;
import com.example.SpringChat.core.user.exception.UserEmailAlreadyExistsException;
import com.example.SpringChat.core.user.exception.UserEmailNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User("testuser", "test@email.com", "hashedPassword", 123456);
        sampleUser.setId(UUID.randomUUID());
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        Mockito.when(userGateway.existsByEmail("test@email.com")).thenReturn(false);
        Mockito.when(userGateway.existsByPublicIdentificationKey(anyInt())).thenReturn(false);
        Mockito.when(userGateway.encodePassword("password123")).thenReturn("hashedPassword");
        Mockito.when(userGateway.save(any(User.class))).thenReturn(sampleUser);

        CreateUserResponseDTO result = userService.register("testuser", "test@email.com", "password123");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("testuser", result.nickname());
        Assertions.assertEquals("test@email.com", result.email());
        Assertions.assertEquals(123456, result.publicIdentificationKey());

        Mockito.verify(userGateway, Mockito.times(1)).existsByEmail("test@email.com");
        Mockito.verify(userGateway, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenRegisteringWithExistingEmail() {
        Mockito.when(userGateway.existsByEmail("test@email.com")).thenReturn(true);

        Assertions.assertThrows(UserEmailAlreadyExistsException.class, () ->
                userService.register("testuser", "test@email.com", "password123")
        );

        Mockito.verify(userGateway, Mockito.never()).save(any(User.class));
    }

    @Test
    void shouldLoginSuccessfully() {
        Mockito.when(userGateway.findByEmail("test@email.com")).thenReturn(Optional.of(sampleUser));
        Mockito.when(userGateway.validatePassword("rawPass", "hashedPassword")).thenReturn(true);
        Mockito.when(userGateway.generateAuthToken(sampleUser)).thenReturn("jwt-token-xyz");

        LoginResponseDTO result = userService.login("test@email.com", "rawPass");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("jwt-token-xyz", result.token());
        Assertions.assertEquals("testuser", result.user().nickname());
        Assertions.assertEquals(123456, result.user().publicIdentificationKey());
    }

    @Test
    void shouldThrowExceptionWhenLoginEmailNotFound() {
        Mockito.when(userGateway.findByEmail("test@email.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserEmailNotFoundException.class, () ->
                userService.login("test@email.com", "rawPass")
        );
    }

    @Test
    void shouldThrowExceptionWhenLoginPasswordDoesNotMatch() {
        Mockito.when(userGateway.findByEmail("test@email.com")).thenReturn(Optional.of(sampleUser));
        Mockito.when(userGateway.validatePassword("wrongPass", "hashedPassword")).thenReturn(false);

        Assertions.assertThrows(PasswordsDoesntMatchesException.class, () ->
                userService.login("test@email.com", "wrongPass")
        );
    }

    @Test
    void shouldSearchUsersWithIdentifierContainingTag() {
        UUID authUserId = UUID.randomUUID();
        Mockito.when(userGateway.findTop10NicknameContainingAndPublicIdentificationKeyContaining(eq("testuser"), eq(123456), eq(authUserId)))
                .thenReturn(List.of(sampleUser));

        List<SearchUserResponseDTO> result = userService.searchUsers("testuser#123456", authUserId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("testuser", result.get(0).nickname());
    }

    @Test
    void shouldSearchUsersWithoutTag() {
        UUID authUserId = UUID.randomUUID();
        Mockito.when(userGateway.findTop10NicknameContainingAndPublicIdentificationKeyContaining(eq("testuser"), eq(0), eq(authUserId)))
                .thenReturn(List.of(sampleUser));

        List<SearchUserResponseDTO> result = userService.searchUsers("testuser", authUserId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("testuser", result.get(0).nickname());
    }
}
