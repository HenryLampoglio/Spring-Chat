package com.example.SpringChat.core.chat.entity;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserChatLink {
    private String userId;
    private String chatId;
    private String chatName;
    private String role;
    private List<String> permissions;
    private String targetUserId;
    private String lastMessageContent;
    private String lastMessageSentAt;
    private String senderId;

    public UserChatLink(String userId, String chatId, String chatName, String role, List<String> permissions) {
        this.userId = userId;
        this.chatId = chatId;
        this.chatName = chatName;
        this.role = role;
        this.permissions = permissions;
    }
}
