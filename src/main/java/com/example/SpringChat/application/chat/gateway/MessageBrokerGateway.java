package com.example.SpringChat.application.chat.gateway;

public interface MessageBrokerGateway {
    void publish(String channel, Object message);
}
