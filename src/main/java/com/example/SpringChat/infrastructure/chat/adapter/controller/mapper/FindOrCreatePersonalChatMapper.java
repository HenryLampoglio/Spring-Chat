package com.example.SpringChat.infrastructure.chat.adapter.controller.mapper;

import com.example.SpringChat.application.chat.responseDTO.FindOrCreatePersonalChatResponseDTO;
import com.example.SpringChat.core.chat.entity.Chat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FindOrCreatePersonalChatMapper {
    FindOrCreatePersonalChatResponseDTO toResponse(Chat chat);
}