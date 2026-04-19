package com.example.SpringChat.core.message.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class Message {
    private String id;
    private UUID chatId;
    private UUID senderId;
    private String senderNickname;
    private String content;
    private Instant sentAt;

    public Message(String id, UUID chatId, UUID senderId, String senderNickname, String content, Instant sentAt) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.sentAt = sentAt;
    }
}
