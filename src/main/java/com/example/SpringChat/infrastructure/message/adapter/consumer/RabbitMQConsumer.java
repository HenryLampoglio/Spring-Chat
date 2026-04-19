package com.example.SpringChat.infrastructure.message.adapter.consumer;

import com.example.SpringChat.application.message.command.SaveMessageCommand;
import com.example.SpringChat.application.message.port.SaveMessageInputPort;
import com.example.SpringChat.core.message.entity.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    private final SaveMessageInputPort saveMessageInputPort;

    public RabbitMQConsumer(SaveMessageInputPort saveMessageInputPort){
        this.saveMessageInputPort = saveMessageInputPort;
    };


    @RabbitListener(queues = "fila.nosql")
    public void consumeMessages(Message message) {

        SaveMessageCommand command = new SaveMessageCommand(message);
        saveMessageInputPort.execute(command);
    }
}
