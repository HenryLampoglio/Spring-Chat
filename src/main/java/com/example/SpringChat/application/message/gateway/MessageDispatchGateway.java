package com.example.SpringChat.application.message.gateway;

import com.example.SpringChat.core.message.entity.Message;

public interface MessageDispatchGateway {
    void dispatchMessage(Message message);
}
