package com.example.SpringChat.infrastructure.chat.persistence.entity;

import com.example.SpringChat.core.chat.entity.Chat;
import com.example.SpringChat.core.enums.ChatAccessType;
import com.example.SpringChat.core.enums.ChatType;
import com.example.SpringChat.infrastructure.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatEntity extends AbstractEntity<UUID> {

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_type", nullable = false, columnDefinition = "chat_type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ChatType chatType;

    @Column(name = "chat_name", length = 150)
    private String chatName;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type", nullable = false, columnDefinition = "access_type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ChatAccessType chatAccessType;

    @Column(length = 255)
    private String description;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    public ChatEntity(Chat chat) {
        this.chatType = chat.getChatType();
        this.chatName = chat.getChatName();
        this.chatAccessType = chat.getChatAccessType();
        this.description = chat.getDescription();
        this.createdBy = chat.getCreatedBy();
    }

    public Chat toCoreChat() {
        Chat chat = new Chat(this.chatType, this.chatName, this.chatAccessType, this.description, this.createdBy);
        chat.setId(this.id);
        chat.setCreatedAt(this.getCreatedAt());
        chat.setUpdatedAt(this.getUpdatedAt());
        return chat;
    }
}