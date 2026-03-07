package com.example.SpringChat.infrastructure.messaging.websocket;

import com.example.SpringChat.application.chat.command.ValidateChatAccessCommand;
import com.example.SpringChat.application.chat.port.ValidateChatAccessInputPort;
import com.example.SpringChat.infrastructure.security.TokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.security.Principal;
import java.util.Collections;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";
    private final TokenService tokenService;
    private final ValidateChatAccessInputPort validateChatAccessInputPort;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        switch (accessor.getCommand()) {
            case CONNECT -> handleConnect(accessor);
            case SUBSCRIBE -> handleSubscribe(accessor);
            default -> {}
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new MessageDeliveryException("Token de autenticação ausente ou malformado");
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        String username = tokenService.validateToken(token);
        String nickname = tokenService.getNicknameFromToken(token);

        if (username == null) {
            throw new MessageDeliveryException("Sessão inválida ou expirada");
        }

        var auth = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
        accessor.setUser(auth);

        Objects.requireNonNull(accessor.getSessionAttributes()).put("nickname", nickname);
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        Principal user = accessor.getUser();

        if (user == null) {
            throw new MessageDeliveryException("Usuário não autenticado");
        }

        try {
            String roomId = extractChatId(accessor.getDestination());
            ValidateChatAccessCommand command = new ValidateChatAccessCommand(user.getName(), roomId);
            validateChatAccessInputPort.execute(command);
        } catch (Exception e) {
            throw new MessageDeliveryException("Falha ao entrar na sala: " + e.getMessage());
        }
    }


    private String extractChatId(String destination){
        AntPathMatcher matcher = new AntPathMatcher();
        String pattern = "/topic/messages/{id}";

        if (destination != null && matcher.match(pattern, destination)) {
            return matcher.extractUriTemplateVariables(pattern, destination).get("id");
        }

        throw new MessageDeliveryException("Destino de inscrição inválido ou malformado.");
    }
}