package com.example.SpringChat.infrastructure.messaging.redis;

import com.example.SpringChat.application.chat.gateway.MessageBrokerGateway;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisPublisherGatewayAdapter implements MessageBrokerGateway {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisPublisherGatewayAdapter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void publish(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
