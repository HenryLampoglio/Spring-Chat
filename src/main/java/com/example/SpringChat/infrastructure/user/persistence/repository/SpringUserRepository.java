package com.example.SpringChat.infrastructure.user.persistence.repository;

import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringUserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);
    boolean existsByPublicIdentificationKey(int publicIdentificationKey);
    Optional<UserEntity> findByEmail(String email);
}
