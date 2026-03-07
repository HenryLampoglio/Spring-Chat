package com.example.SpringChat.application.chat.gateways;

public interface MessageBrokerGateway {
    void publish(String channel, Object message);
}
