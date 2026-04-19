package com.example.SpringChat.application.chat.usecase;

import com.example.SpringChat.application.chat.command.SendMessageCommand;
import com.example.SpringChat.application.chat.gateway.MessageBrokerGateway;
import com.example.SpringChat.application.chat.port.SendMessageInputPort;
import com.example.SpringChat.application.chat.responseDTO.ChatMessageResponseDTO;
import com.example.SpringChat.application.message.gateway.MessageDispatchGateway;
import com.example.SpringChat.core.chat.gateway.ChatGateway;
import com.example.SpringChat.core.message.entity.Message;

import java.time.Instant;

public class SendMessageUseCase implements SendMessageInputPort {

    private final ChatGateway chatGateway;
    private final MessageBrokerGateway messageBrokerGateway;
    private final MessageDispatchGateway messageDispatchGateway;

    public SendMessageUseCase(ChatGateway chatGateway, MessageBrokerGateway messageBrokerGateway, MessageDispatchGateway messageDispatchGateway){
        this.chatGateway = chatGateway;
        this.messageBrokerGateway = messageBrokerGateway;
        this.messageDispatchGateway = messageDispatchGateway;
    }

    @Override
    public void execute(SendMessageCommand command){

        String messageId = chatGateway.generateNewMessageUlid();
        Instant now = Instant.now();

        Message message = new Message(
                messageId,
                command.chatId(),
                command.senderId(),
                command.senderNickname(),
                command.message(),
                now
        );

        messageDispatchGateway.dispatchMessage(message);

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