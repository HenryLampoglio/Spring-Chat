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

    public java.util.List<Message> findMessagesByChatId(java.util.UUID chatId, String beforeUlid, int limit) {
        String pk = "CHAT#" + chatId;
        String condition;
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":pk", AttributeValue.builder().s(pk).build());

        if (beforeUlid != null && !beforeUlid.isBlank()) {
            condition = "PK = :pk AND SK < :beforeSk";
            expressionValues.put(":beforeSk", AttributeValue.builder().s("MSG#" + beforeUlid).build());
        } else {
            condition = "PK = :pk AND begins_with(SK, :skPrefix)";
            expressionValues.put(":skPrefix", AttributeValue.builder().s("MSG#").build());
        }

        software.amazon.awssdk.services.dynamodb.model.QueryRequest request = software.amazon.awssdk.services.dynamodb.model.QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression(condition)
                .expressionAttributeValues(expressionValues)
                .scanIndexForward(false)
                .limit(limit > 0 ? limit : 50)
                .build();

        software.amazon.awssdk.services.dynamodb.model.QueryResponse response = dynamoDbClient.query(request);
        return response.items().stream()
                .map(item -> new Message(
                        item.get("SK").s().replace("MSG#", ""),
                        chatId,
                        java.util.UUID.fromString(item.get("senderId").s()),
                        item.get("senderNickname").s(),
                        item.get("content").s(),
                        java.time.Instant.parse(item.get("sentAt").s())
                ))
                .toList();
    }
}
