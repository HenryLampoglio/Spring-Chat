package com.example.SpringChat.infrastructure.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    public record DTOMessage(String content){}

    @MessageMapping("/hello")
    @SendTo("/topic/messages")
    public DTOMessage hello(DTOMessage message){
        return new DTOMessage("Echo: " + message.content());
    }
}