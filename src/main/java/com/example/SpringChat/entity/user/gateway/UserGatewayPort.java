package com.example.SpringChat.entity.user.gateway;

import com.example.SpringChat.entity.user.model.User;

import java.util.Optional;

public interface UserGatewayPort {

    boolean UserEmailExists(String email);

    User save(User user);

    Optional<User> findByEmail(String email);
}
