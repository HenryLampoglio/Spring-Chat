package com.example.SpringChat.infrastructure.chat.persistence.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.List;

@DynamoDbBean
public class ChatSystemDynamoEntity {

    private String pk;
    private String sk;
    private String chatId;
    private String targetUserId;
    private List<String> participantIds;
    private String chatName;
    private String role;
    private List<String> permissions;
    private String lastMessageContent;
    private String lastMessageSentAt;
    private String senderId;

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "GSI_User_Sidebar")
    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @DynamoDbSortKey
    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    @DynamoDbSecondarySortKey(indexNames = "GSI_User_Sidebar")
    public String getLastMessageSentAt() {
        return lastMessageSentAt;
    }

    public void setLastMessageSentAt(String lastMessageSentAt) {
        this.lastMessageSentAt = lastMessageSentAt;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
