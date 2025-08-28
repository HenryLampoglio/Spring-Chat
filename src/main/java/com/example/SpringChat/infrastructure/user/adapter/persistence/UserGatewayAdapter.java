package com.example.SpringChat.infrastructure.user.adapter.persistence;

import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.gateway.UserGateway;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import com.example.SpringChat.infrastructure.user.persistence.repository.SpringUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserGatewayAdapter implements UserGateway {
    private final SpringUserRepository springUserRepository;

    public UserGatewayAdapter(SpringUserRepository springUserRepository){
        this.springUserRepository = springUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = new UserEntity(user);
        UserEntity savedEntity = springUserRepository.save(userEntity);
        return savedEntity.toCoreUser();
    }

    @Override
    public boolean existsByEmail(String email) {
        return springUserRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPublicIdentificationKey(int code){
        return springUserRepository.existsByPublicIdentificationKey(code);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springUserRepository.findByEmail(email).map(UserEntity::toCoreUser);
    }

}
