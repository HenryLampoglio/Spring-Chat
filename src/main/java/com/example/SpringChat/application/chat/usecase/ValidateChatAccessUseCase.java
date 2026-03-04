package com.example.SpringChat.application.chat.usecase;


import com.example.SpringChat.application.chat.command.ValidateChatAccessCommand;
import com.example.SpringChat.application.chat.port.ValidateChatAccessInputPort;
import com.example.SpringChat.core.chat.exception.ChatAccessDeniedException;
import com.example.SpringChat.core.chat.gateway.ChatGateway;

public class ValidateChatAccessUseCase implements ValidateChatAccessInputPort {
    private final ChatGateway chatGateway;

    public ValidateChatAccessUseCase(ChatGateway chatGateway){this.chatGateway = chatGateway;}

    @Override
    public void execute(ValidateChatAccessCommand command){
        Boolean hasAccess = chatGateway.verifyUserChatAccess(command.userEmail(), command.chatId());

        if (!hasAccess) {
            throw new ChatAccessDeniedException(command.chatId());
        }
    }
}
