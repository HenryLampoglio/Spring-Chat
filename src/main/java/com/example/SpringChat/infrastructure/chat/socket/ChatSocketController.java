package com.example.SpringChat.infrastructure.chat.socket;

import com.example.SpringChat.application.chat.requestDTO.SendMessageRequest;
import com.example.SpringChat.application.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

@Controller
public class ChatSocketController {

    private final ChatService chatService;

    public ChatSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{chatId}/send")
    public void sendMessage(
            @DestinationVariable UUID chatId,
            SendMessageRequest message,
            SimpMessageHeaderAccessor headerAccessor,
            Principal principal
    ) {
        UUID senderId = UUID.fromString(principal.getName());
        String nickname = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("nickname");

        chatService.sendMessage(chatId, senderId, nickname, message.content());
    }
}