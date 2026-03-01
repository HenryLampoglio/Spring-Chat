package com.example.SpringChat.infrastructure.chat.adapter.persistence;

import com.example.SpringChat.core.chat.entity.Chat;
import com.example.SpringChat.core.chat.gateway.ChatGateway;
import com.example.SpringChat.core.enums.ChatAccessType;
import com.example.SpringChat.core.enums.ChatType;
import com.example.SpringChat.infrastructure.chat.persistence.entity.ChatEntity;
import com.example.SpringChat.infrastructure.chat.persistence.entity.UserChatEntity;
import com.example.SpringChat.infrastructure.chat.persistence.repository.SpringChatRepository;
import com.example.SpringChat.infrastructure.chat.persistence.repository.SpringUserChatRepository;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import com.example.SpringChat.infrastructure.user.persistence.repository.SpringUserRepository;
import com.example.SpringChat.core.user.exception.UserNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class ChatGatewayAdapter implements ChatGateway {

    private final SpringChatRepository springChatRepository;
    private final SpringUserChatRepository springUserChatRepository;
    private final SpringUserRepository springUserRepository;

    public ChatGatewayAdapter(
            SpringChatRepository chatRepository,
            SpringUserChatRepository springUserChatRepository,
            SpringUserRepository userRepository
    ) {
        this.springChatRepository = chatRepository;
        this.springUserChatRepository = springUserChatRepository;
        this.springUserRepository = userRepository;
    }

    @Override
    public Optional<Chat> findPersonalChatByUsers(UUID userIdA, UUID userIdB) {
        return springChatRepository.findPersonalChatByUsers(userIdA, userIdB)
                .map(ChatEntity::toCoreChat);
    }

    @Override
    @Transactional
    public Chat createPersonalChat(UUID userIdA, UUID userIdB) {
        UserEntity userA = springUserRepository.findById(userIdA)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userIdA));

        UserEntity userB = springUserRepository.findById(userIdB)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userIdB));

        ChatEntity chat = new ChatEntity(ChatType.PERSONAL, null, ChatAccessType.PRIVATE, null, userIdA);
        ChatEntity savedChat = springChatRepository.saveAndFlush(chat);

        springUserChatRepository.save(new UserChatEntity(userA, savedChat));
        springUserChatRepository.save(new UserChatEntity(userB, savedChat));

        return savedChat.toCoreChat();
    }
}