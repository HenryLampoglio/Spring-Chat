package com.example.SpringChat.application.message.usecase;

import com.example.SpringChat.application.message.command.SaveMessageCommand;
import com.example.SpringChat.application.message.gateway.MessageDispatchGateway;
import com.example.SpringChat.application.message.gateway.MessagePersistenceGateway;
import com.example.SpringChat.application.message.port.SaveMessageInputPort;

public class SaveMessageUseCase implements SaveMessageInputPort {
    private final MessagePersistenceGateway messagePersistenceGateway;

    public SaveMessageUseCase(MessagePersistenceGateway messagePersistenceGateway) {
        this.messagePersistenceGateway = messagePersistenceGateway;
    }

    @Override
    public void execute(SaveMessageCommand saveMessageCommand) {

        messagePersistenceGateway.saveMessage(saveMessageCommand.message());
    }
}
