package com.example.SpringChat.core.user.gateway;

import com.example.SpringChat.core.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGateway {

    boolean userExists(UUID id);

    boolean existsByEmail(String email);

    boolean existsByPublicIdentificationKey(int publicIdentificationKey);

    User save(User user);

    Optional<User> findByEmail(String email);

    List<User> findTop10NicknameContainingAndPublicIdentificationKeyContaining(String nickname,int publicIdentificationKey, UUID userId);

    Boolean validatePassword(String password, String hashedPassword);

    String generateAuthToken(User user);

    String encodePassword(String password);

}
