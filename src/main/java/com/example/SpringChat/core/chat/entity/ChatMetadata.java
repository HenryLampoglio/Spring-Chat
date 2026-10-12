package com.example.SpringChat.core.chat.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ChatMetadata {
    private String chatId;
    private String lastMessageContent;
    private String lastMessageSentAt;
    private String senderId;
}
