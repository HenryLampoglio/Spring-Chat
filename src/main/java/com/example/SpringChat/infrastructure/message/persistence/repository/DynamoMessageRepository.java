package com.example.SpringChat.infrastructure.message.persistence.repository;

import com.example.SpringChat.core.message.entity.Message;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DynamoMessageRepository {
    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "ChatMessages";

    public DynamoMessageRepository(DynamoDbClient dynamoDbClient){
        this.dynamoDbClient = dynamoDbClient;
    }

    public void save(Message message) {
        Map<String, AttributeValue> item = new HashMap<>();

        item.put("PK", AttributeValue.builder().s("CHAT#" + message.getChatId()).build());
        item.put("SK", AttributeValue.builder().s("MSG#" + message.getId()).build());


        item.put("senderId", AttributeValue.builder().s(message.getSenderId().toString()).build());
        item.put("senderNickname", AttributeValue.builder().s(message.getSenderNickname()).build());
        item.put("content", AttributeValue.builder().s(message.getContent()).build());
        item.put("sentAt", AttributeValue.builder().s(message.getSentAt().toString()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }
}
