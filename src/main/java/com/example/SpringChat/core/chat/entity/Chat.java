package com.example.SpringChat.core.chat.entity;

import com.example.SpringChat.core.AbstractEntity;
import com.example.SpringChat.core.enums.ChatAccessType;
import com.example.SpringChat.core.enums.ChatType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class Chat extends AbstractEntity<UUID> {
    private ChatType chatType;
    private String chatName;
    private ChatAccessType chatAccessType;
    private String description;
    private UUID createdBy;

    public Chat(ChatType chatType, String chatName, ChatAccessType chatAccessType, String description, UUID createdBy){
        this.chatType = chatType;
        this.chatName = chatName;
        this.chatAccessType = chatAccessType;
        this.description = description;
        this.createdBy = createdBy;
    }
}
