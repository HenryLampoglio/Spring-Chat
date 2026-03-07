package com.example.SpringChat.infrastructure.messaging.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageForwarder implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisMessageForwarder(SimpMessagingTemplate messagingTemplate,
                                 RedisTemplate<String, Object> redisTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Object body = redisTemplate.getValueSerializer().deserialize(message.getBody());
        String channel = (String) redisTemplate.getKeySerializer().deserialize(message.getChannel());

        if (channel != null && channel.startsWith("chat.")) {
            String chatId = channel.substring(5);
            String websocketDestination = "/topic/messages/" + chatId;

            messagingTemplate.convertAndSend(websocketDestination, body);
        }
    }
}
