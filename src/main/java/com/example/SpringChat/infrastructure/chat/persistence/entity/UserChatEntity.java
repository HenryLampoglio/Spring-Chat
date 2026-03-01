package com.example.SpringChat.infrastructure.chat.persistence.entity;

import com.example.SpringChat.infrastructure.AbstractEntity;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "user_chats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "chat_id"})
)
@NoArgsConstructor
@Getter
@Setter
public class UserChatEntity extends AbstractEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatEntity chat;

    public UserChatEntity(UserEntity user, ChatEntity chat) {
        this.user = user;
        this.chat = chat;
    }
}