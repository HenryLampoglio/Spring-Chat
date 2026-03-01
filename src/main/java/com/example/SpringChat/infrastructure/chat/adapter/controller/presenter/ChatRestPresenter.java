package com.example.SpringChat.infrastructure.chat.adapter.controller.presenter;

import com.example.SpringChat.application.chat.responseDTO.FindOrCreatePersonalChatResponseDTO;
import com.example.SpringChat.core.chat.entity.Chat;
import com.example.SpringChat.infrastructure.chat.adapter.controller.mapper.FindOrCreatePersonalChatMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatRestPresenter {

    private final FindOrCreatePersonalChatMapper findOrCreatePersonalChatMapper;

    public ChatRestPresenter(FindOrCreatePersonalChatMapper findOrCreatePersonalChatMapper) {
        this.findOrCreatePersonalChatMapper = findOrCreatePersonalChatMapper;
    }

    public FindOrCreatePersonalChatResponseDTO toFindOrCreatePersonalChatResponse(Chat chat) {
        return findOrCreatePersonalChatMapper.toResponse(chat);
    }
}