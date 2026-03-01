package com.example.SpringChat.infrastructure.chat.persistence.repository;

import com.example.SpringChat.infrastructure.chat.persistence.entity.UserChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringUserChatRepository extends JpaRepository<UserChatEntity, UUID> {
}