package com.example.SpringChat.application.chat.service;

import com.example.SpringChat.application.chat.gateway.ChatGateway;
import com.example.SpringChat.application.chat.gateway.MessageBrokerGateway;
import com.example.SpringChat.application.chat.responseDTO.ChatMessageResponseDTO;
import com.example.SpringChat.application.chat.responseDTO.SidebarItemResponseDTO;
import com.example.SpringChat.application.message.gateway.MessageDispatchGateway;
import com.example.SpringChat.application.message.gateway.MessagePersistenceGateway;
import com.example.SpringChat.application.user.gateway.UserGateway;
import com.example.SpringChat.core.chat.entity.Chat;
import com.example.SpringChat.core.chat.entity.ChatMetadata;
import com.example.SpringChat.core.chat.entity.UserChatLink;
import com.example.SpringChat.core.chat.exception.ChatAccessDeniedException;
import com.example.SpringChat.core.message.entity.Message;
import com.example.SpringChat.core.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatGateway chatGateway;
    private final MessageBrokerGateway messageBrokerGateway;
    private final MessageDispatchGateway messageDispatchGateway;
    private final MessagePersistenceGateway messagePersistenceGateway;
    private final UserGateway userGateway;

    public ChatService(
            ChatGateway chatGateway,
            MessageBrokerGateway messageBrokerGateway,
            MessageDispatchGateway messageDispatchGateway,
            MessagePersistenceGateway messagePersistenceGateway,
            UserGateway userGateway
    ) {
        this.chatGateway = chatGateway;
        this.messageBrokerGateway = messageBrokerGateway;
        this.messageDispatchGateway = messageDispatchGateway;
        this.messagePersistenceGateway = messagePersistenceGateway;
        this.userGateway = userGateway;
    }

    public Chat findOrCreatePersonalChat(UUID authenticatedUserId, UUID targetUserId) {
        return chatGateway.findPersonalChatByUsers(authenticatedUserId, targetUserId)
                .orElseGet(() -> chatGateway.createPersonalChat(authenticatedUserId, targetUserId));
    }

    public void validateChatAccess(String userId, String chatId) {
        Boolean hasAccess = chatGateway.verifyUserChatAccess(userId, chatId);
        if (hasAccess == null || !hasAccess) {
            throw new ChatAccessDeniedException("Usuário não tem acesso ao chat: " + chatId);
        }
    }

    public void sendMessage(UUID chatId, UUID senderId, String nickname, String content) {
        String messageId = chatGateway.generateNewMessageUlid();
        Instant now = Instant.now();

        // 1. Cria entidade de mensagem de domínio
        Message message = new Message(messageId, chatId, senderId, nickname, content, now);

        // 2. Despacha assincronamente para a fila RabbitMQ (para ser salva no DynamoDB)
        messageDispatchGateway.dispatchMessage(message);

        // 3. Publica no Redis para distribuição em tempo real via WebSocket STOMP
        ChatMessageResponseDTO payload = new ChatMessageResponseDTO(
                messageId,
                chatId,
                senderId,
                nickname,
                content,
                now.toString()
        );
        String channel = "chat." + chatId;
        messageBrokerGateway.publish(channel, payload);

        // 4. Atualiza metadados do chat e vínculos dos usuários para manter o GSI da Sidebar atualizado
        chatGateway.updateLastMessage(chatId.toString(), content, now.toString(), senderId.toString());
    }

    public List<SidebarItemResponseDTO> loadSidebar(UUID userId) {
        List<UserChatLink> userChats = chatGateway.findUserChats(userId.toString());
        if (userChats.isEmpty()) {
            return Collections.emptyList();
        }

        boolean hasMetadataInLinks = userChats.stream().anyMatch(link -> link.getLastMessageSentAt() != null);
        Map<String, ChatMetadata> metadataMap = new HashMap<>();

        if (!hasMetadataInLinks) {
            List<String> chatIds = userChats.stream().map(UserChatLink::getChatId).toList();
            List<ChatMetadata> metadatas = chatGateway.findChatsMetadata(chatIds);
            metadataMap = metadatas.stream().collect(Collectors.toMap(ChatMetadata::getChatId, m -> m, (m1, m2) -> m1));
        }

        Set<UUID> userIdsToFetch = new HashSet<>();
        for (UserChatLink link : userChats) {
            String senderIdStr = link.getSenderId();
            if (senderIdStr == null || senderIdStr.isBlank()) {
                ChatMetadata meta = metadataMap.get(link.getChatId());
                if (meta != null) {
                    senderIdStr = meta.getSenderId();
                }
            }
            if (senderIdStr != null && !senderIdStr.isBlank()) {
                try {
                    userIdsToFetch.add(UUID.fromString(senderIdStr));
                } catch (IllegalArgumentException ignored) {}
            }
            if (link.getTargetUserId() != null && !link.getTargetUserId().isBlank()) {
                try {
                    userIdsToFetch.add(UUID.fromString(link.getTargetUserId()));
                } catch (IllegalArgumentException ignored) {}
            }
        }

        Map<UUID, String> nicknameMap = new HashMap<>();
        if (!userIdsToFetch.isEmpty()) {
            List<User> users = userGateway.findByIds(new ArrayList<>(userIdsToFetch));
            nicknameMap = users.stream().collect(Collectors.toMap(User::getId, User::getNickname, (n1, n2) -> n1));
        }

        Map<UUID, String> finalNicknameMap = nicknameMap;
        Map<String, ChatMetadata> finalMetadataMap = metadataMap;

        return userChats.stream().map(chatLink -> {
            String lastMessageContent = chatLink.getLastMessageContent();
            String lastMessageSentAt = chatLink.getLastMessageSentAt();
            String senderIdStr = chatLink.getSenderId();

            if (lastMessageSentAt == null) {
                ChatMetadata meta = finalMetadataMap.get(chatLink.getChatId());
                if (meta != null) {
                    lastMessageContent = meta.getLastMessageContent();
                    lastMessageSentAt = meta.getLastMessageSentAt();
                    senderIdStr = meta.getSenderId();
                }
            }

            String senderNickname = null;
            if (senderIdStr != null && !senderIdStr.isBlank()) {
                try {
                    senderNickname = finalNicknameMap.get(UUID.fromString(senderIdStr));
                } catch (IllegalArgumentException ignored) {}
            }

            String displayName = chatLink.getChatName();
            if ((displayName == null || displayName.isBlank()) && chatLink.getTargetUserId() != null) {
                try {
                    displayName = finalNicknameMap.get(UUID.fromString(chatLink.getTargetUserId()));
                } catch (IllegalArgumentException ignored) {}
            }

            return new SidebarItemResponseDTO(
                    chatLink.getChatId(),
                    displayName,
                    chatLink.getRole(),
                    lastMessageContent,
                    lastMessageSentAt,
                    senderNickname
            );
        }).toList();
    }

    public List<ChatMessageResponseDTO> findChatHistory(UUID chatId, String beforeUlid, int limit) {
        List<Message> messages = messagePersistenceGateway.findMessagesByChatId(chatId, beforeUlid, limit);
        return messages.stream()
                .map(m -> new ChatMessageResponseDTO(
                        m.getId(),
                        m.getChatId(),
                        m.getSenderId(),
                        m.getSenderNickname(),
                        m.getContent(),
                        m.getSentAt().toString()
                ))
                .toList();
    }
}
