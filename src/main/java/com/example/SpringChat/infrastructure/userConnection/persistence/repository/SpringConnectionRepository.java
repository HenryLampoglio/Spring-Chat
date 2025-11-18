package com.example.SpringChat.infrastructure.userConnection.persistence.repository;

import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.infrastructure.userConnection.persistence.entity.ConnectionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringConnectionRepository extends JpaRepository<ConnectionEntity, UUID> {

    @Query(value = "SELECT DISTINCT c FROM ConnectionEntity c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.friend " +
            "WHERE c.user.id = :userId OR c.friend.id = :userId AND c.connectionStatus = :connectionAccepted",
            countQuery = "SELECT count(c) FROM ConnectionEntity c " +
                    "WHERE c.user.id = :userId OR c.friend.id = :userId")
    Page<ConnectionEntity> findAllByUserIdOrFriendIdWithUsers(@Param("connectionAccepted") ConnectionStatus status,@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT c FROM ConnectionEntity c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.friend " +
            "WHERE (c.user.id = :userId OR c.friend.id = :userId) " +
            "AND c.connectionStatus = :status")
    List<ConnectionEntity> findAllByUserIdAndStatusWithUsers(
            @Param("userId") UUID userId,
            @Param("status") ConnectionStatus status
    );

    @Query("SELECT c FROM ConnectionEntity c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.friend " +
            "WHERE c.friend.id = :friendId")
    List<ConnectionEntity> findAllByFriendIdWithUsers(@Param("friendId") UUID friendId);
}
