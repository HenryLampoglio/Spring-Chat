package com.example.SpringChat.core.user.gateway;

import com.example.SpringChat.core.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGateway {

    boolean existsByEmail(String email);

    boolean existsByPublicIdentificationKey(int publicIdentificationKey);

    User save(User user);

    Optional<User> findByEmail(String email);

    List<User> findFriendsByUserId(UUID userId);
}
