package com.example.SpringChat.infrastructure.user.adapter.persistence;

import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.gateway.UserGateway;
import com.example.SpringChat.infrastructure.security.TokenService;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import com.example.SpringChat.infrastructure.user.persistence.repository.SpringUserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserGatewayAdapter implements UserGateway {
    private final SpringUserRepository springUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    public UserGatewayAdapter(SpringUserRepository springUserRepository, BCryptPasswordEncoder passwordEncoder, TokenService tokenService){
        this.springUserRepository = springUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = new UserEntity(user);
        UserEntity savedEntity = springUserRepository.save(userEntity);
        return savedEntity.toCoreUser();
    }

    @Override
    public boolean userExists(UUID id){
        return springUserRepository.existsById(id);
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

    @Override
    public List<User> findTop10NicknameContainingAndPublicIdentificationKeyContaining(String nickname, int publicIdentificationKey, UUID userId){
        List<UserEntity> entityList = springUserRepository.findTop10NicknameContainingAndPublicIdentificationKeyContaining(nickname, publicIdentificationKey, userId,PageRequest.of(0,10));

        return entityList.stream().map(UserEntity::toCoreUser).toList();
    }

    @Override
    public Boolean validatePassword(String password, String hashedPassword){
        return passwordEncoder.matches(password, hashedPassword);
    }

    @Override
    public String generateAuthToken(User user){
        return tokenService.generateToken(user);
    }

    @Override
    public String encodePassword(String password){
     return passwordEncoder.encode(password);
    }
}
