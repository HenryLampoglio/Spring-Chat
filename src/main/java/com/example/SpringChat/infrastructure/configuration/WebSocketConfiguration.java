package com.example.SpringChat.infrastructure.configuration;

import com.example.SpringChat.infrastructure.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    private TaskScheduler messageBrokeTaskScheduler;
    private final TokenService tokenService;

    public WebSocketConfiguration(TokenService tokenService){
        this.tokenService = tokenService;
    }

    @Autowired
    public void setMessageBrokeTaskScheduler(@Lazy TaskScheduler taskScheduler){
        this.messageBrokeTaskScheduler = taskScheduler;
    }

    @Override
    public  void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic")
                .setHeartbeatValue(new long[] {10000, 20000})
                .setTaskScheduler(this.messageBrokeTaskScheduler);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                // Transforma a mensagem genérica em algo legível (STOMP)
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Verifica: É uma mensagem de "QUERO CONECTAR"?
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    // Pega o header que você mandou no Angular: Authorization: Bearer ...
                    String authHeader = accessor.getFirstNativeHeader("Authorization");

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);

                        // --- VALIDAÇÃO ---
                        // Aqui você chama seu serviço para validar o token
                        String user = tokenService.validateToken(token);

                        if (user != null) {
                            // Cria a identidade do usuário para o Spring Security
                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

                            accessor.setUser(auth);
                        }
                    }
                }
                return message;
            }
        });
    }
}
