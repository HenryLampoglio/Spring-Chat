package com.example.SpringChat.infrastructure.chat.persistence.repository;

import com.example.SpringChat.infrastructure.chat.persistence.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringChatRepository extends JpaRepository<ChatEntity, UUID> {

    @Query("""
        SELECT c FROM ChatEntity c
        JOIN UserChatEntity uc1 ON uc1.chat.id = c.id
        JOIN UserChatEntity uc2 ON uc2.chat.id = c.id
        WHERE uc1.user.id = :userIdA
        AND uc2.user.id = :userIdB
        AND c.chatType = 'PERSONAL'
    """)
    Optional<ChatEntity> findPersonalChatByUsers(@Param("userIdA") UUID userIdA, @Param("userIdB") UUID userIdB);
}