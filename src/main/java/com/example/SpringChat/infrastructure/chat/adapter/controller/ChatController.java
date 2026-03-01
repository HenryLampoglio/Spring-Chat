package com.example.SpringChat.infrastructure.chat.adapter.controller;

import com.example.SpringChat.application.chat.command.FindOrCreatePersonalChatCommand;
import com.example.SpringChat.application.chat.port.FindOrCreatePersonalChatInputPort;
import com.example.SpringChat.application.chat.responseDTO.FindOrCreatePersonalChatResponseDTO;
import com.example.SpringChat.core.chat.entity.Chat;
import com.example.SpringChat.infrastructure.chat.adapter.controller.presenter.ChatRestPresenter;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("chats")
public class ChatController {

    private final FindOrCreatePersonalChatInputPort findOrCreatePersonalChatInputPort;
    private final ChatRestPresenter chatRestPresenter;

    public ChatController(
            FindOrCreatePersonalChatInputPort findOrCreatePersonalChatInputPort,
            ChatRestPresenter chatRestPresenter
    ) {
        this.findOrCreatePersonalChatInputPort = findOrCreatePersonalChatInputPort;
        this.chatRestPresenter = chatRestPresenter;
    }

    @GetMapping("/{targetUserId}")
    public ResponseEntity<FindOrCreatePersonalChatResponseDTO> findOrCreateChat(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable UUID targetUserId
    ) {
        FindOrCreatePersonalChatCommand command = new FindOrCreatePersonalChatCommand(user.getId(), targetUserId);
        Chat chat = findOrCreatePersonalChatInputPort.execute(command);
        FindOrCreatePersonalChatResponseDTO response = chatRestPresenter.toFindOrCreatePersonalChatResponse(chat);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
