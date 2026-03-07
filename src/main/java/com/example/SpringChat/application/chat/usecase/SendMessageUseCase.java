package com.example.SpringChat.application.chat.usecase;

import com.example.SpringChat.application.chat.command.SendMessageCommand;
import com.example.SpringChat.application.chat.gateways.MessageBrokerGateway;
import com.example.SpringChat.application.chat.port.SendMessageInputPort;
import com.example.SpringChat.application.chat.responseDTO.ChatMessageResponseDTO;
import com.example.SpringChat.core.chat.gateway.ChatGateway;

import java.time.LocalDateTime;

public class SendMessageUseCase implements SendMessageInputPort {

    private final ChatGateway chatGateway;
    private final MessageBrokerGateway messageBrokerGateway;

    public SendMessageUseCase(ChatGateway chatGateway, MessageBrokerGateway messageBrokerGateway){
        this.chatGateway = chatGateway;
        this.messageBrokerGateway = messageBrokerGateway;
    }

    @Override
    public void execute(SendMessageCommand command){

//        Message message = new Message(
//                command.chatId(),
//                command.senderId(),
//                command.message()
//        );

//        Message savedMessage = chatGateway.saveMessage(message);

        String formatedDate = java.time.LocalDateTime.now().toString();

        ChatMessageResponseDTO responsePayload = new ChatMessageResponseDTO(
                command.chatId(),
                command.senderId(),
                command.senderNickname(),
                command.message(),
                formatedDate
        );

        // 4. Define o canal de destino e publica no Redis
        String redisChannel = "chat." + command.chatId();
        messageBrokerGateway.publish(redisChannel, responsePayload);
    }
}