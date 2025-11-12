package com.example.SpringChat.infrastructure.user.persistence.repository;

import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringUserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);
    boolean existsByPublicIdentificationKey(int publicIdentificationKey);
    Optional<UserEntity> findByEmail(String email);

    @Query("Select u FROM UserEntity u WHERE " +
        "(:nickname = '' OR u.nickname LIKE CONCAT('%', :nickname, '%')) AND " +
        "(:publicIdentificationKey = 0 OR u.publicIdentificationKey = :publicIdentificationKey)")
    List<UserEntity> findTop10NicknameContainingAndPublicIdentificationKeyContaining(
            @Param("nickname") String nickname,
            @Param("publicIdentificationKey") int publicIdentificationKey,
            Pageable pageable);
}
