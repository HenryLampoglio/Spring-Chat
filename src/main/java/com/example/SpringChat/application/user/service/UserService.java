package com.example.SpringChat.application.user.service;

import com.example.SpringChat.application.user.gateway.UserGateway;
import com.example.SpringChat.application.user.responseDTO.CreateUserResponseDTO;
import com.example.SpringChat.application.user.responseDTO.LoginResponseDTO;
import com.example.SpringChat.application.user.responseDTO.SearchUserResponseDTO;
import com.example.SpringChat.application.user.responseDTO.UserDataResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.PasswordsDoesntMatchesException;
import com.example.SpringChat.core.user.exception.UserEmailAlreadyExistsException;
import com.example.SpringChat.core.user.exception.UserEmailNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {

    private final UserGateway userGateway;
    private final Random random = new Random();

    public UserService(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public CreateUserResponseDTO register(String nickname, String email, String password) {
        if (userGateway.existsByEmail(email)) {
            throw new UserEmailAlreadyExistsException("O e-mail " + email + " Já está em uso.");
        }

        int publicIdentificationKey;
        do {
            publicIdentificationKey = random.nextInt(900000) + 100000;
        } while (userGateway.existsByPublicIdentificationKey(publicIdentificationKey));

        String hashedPassword = userGateway.encodePassword(password);
        User newUser = new User(nickname, email, hashedPassword, publicIdentificationKey);
        User savedUser = userGateway.save(newUser);

        return new CreateUserResponseDTO(
                savedUser.getId(),
                savedUser.getNickname(),
                savedUser.getEmail(),
                savedUser.getPublicIdentificationKey(),
                savedUser.getCreatedAt()
        );
    }

    public LoginResponseDTO login(String email, String password) {
        User user = userGateway.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("Usuário com esse email não encontrado"));

        boolean passwordMatches = userGateway.validatePassword(password, user.getHashedPassword());
        if (!passwordMatches) {
            throw new PasswordsDoesntMatchesException("Senha Incorreta.");
        }

        String token = userGateway.generateAuthToken(user);
        UserDataResponseDTO userData = new UserDataResponseDTO(
                user.getId(),
                user.getNickname(),
                user.getPublicIdentificationKey(),
                user.getUserQuote()
        );

        return new LoginResponseDTO(token, userData);
    }

    public List<SearchUserResponseDTO> searchUsers(String userIdentifier, UUID authenticatedUserId) {
        String nicknamePart = userIdentifier;
        int keyPart = 0;

        if (userIdentifier != null) {
            int separatorIndex = userIdentifier.indexOf('#');
            if (separatorIndex != -1) {
                nicknamePart = userIdentifier.substring(0, separatorIndex);
                if (separatorIndex < userIdentifier.length() - 1) {
                    try {
                        keyPart = Integer.parseInt(userIdentifier.substring(separatorIndex + 1));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        List<User> users = userGateway.findTop10NicknameContainingAndPublicIdentificationKeyContaining(
                nicknamePart,
                keyPart,
                authenticatedUserId
        );

        return users.stream()
                .map(u -> new SearchUserResponseDTO(
                        u.getId(),
                        u.getNickname(),
                        u.getPublicIdentificationKey(),
                        u.getCreatedAt(),
                        u.getUpdatedAt()
                ))
                .toList();
    }
}
