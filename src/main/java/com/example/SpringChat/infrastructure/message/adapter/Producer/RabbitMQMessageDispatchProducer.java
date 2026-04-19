package com.example.SpringChat.infrastructure.message.adapter.Producer;

import com.example.SpringChat.application.message.gateway.MessageDispatchGateway;
import com.example.SpringChat.core.message.entity.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageDispatchProducer implements MessageDispatchGateway {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQMessageDispatchProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void dispatchMessage(Message message){
        System.out.println("====== [DEBUG] CHEGOU NO PRODUTOR DO RABBITMQ ======");
        System.out.println("Mensagem: " + message.toString());
        rabbitTemplate.convertAndSend("exchange.direta", "rota.salvar.nosql", message);
        System.out.println("====== [DEBUG] ENVIADO PARA A EXCHANGE ======");
    }
}
