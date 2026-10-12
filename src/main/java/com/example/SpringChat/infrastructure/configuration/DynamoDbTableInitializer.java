package com.example.SpringChat.infrastructure.configuration;

import com.example.SpringChat.infrastructure.chat.persistence.entity.ChatSystemDynamoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Component
public class DynamoDbTableInitializer {

    private static final Logger log = LoggerFactory.getLogger(DynamoDbTableInitializer.class);

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbEnhancedClient enhancedClient;

    public DynamoDbTableInitializer(DynamoDbClient dynamoDbClient, DynamoDbEnhancedClient enhancedClient) {
        this.dynamoDbClient = dynamoDbClient;
        this.enhancedClient = enhancedClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeTables() {
        initChatSystemTable();
        initChatMessagesTable();
    }

    private void initChatSystemTable() {
        try {
            DynamoDbTable<ChatSystemDynamoEntity> table = enhancedClient.table(
                    "Chat_System", TableSchema.fromBean(ChatSystemDynamoEntity.class)
            );
            try {
                table.describeTable();
                log.info("DynamoDB table 'Chat_System' already exists.");
            } catch (ResourceNotFoundException e) {
                log.info("Creating DynamoDB table 'Chat_System' with GSI_User_Sidebar...");
                table.createTable(r -> r
                        .provisionedThroughput(b -> b.readCapacityUnits(5L).writeCapacityUnits(5L))
                        .globalSecondaryIndices(gsi -> gsi
                                .indexName("GSI_User_Sidebar")
                                .projection(p -> p.projectionType(ProjectionType.ALL))
                                .provisionedThroughput(b -> b.readCapacityUnits(5L).writeCapacityUnits(5L))
                        )
                );
                log.info("DynamoDB table 'Chat_System' created successfully.");
            }
        } catch (Exception e) {
            log.warn("Could not check/create 'Chat_System' table: {}", e.getMessage());
        }
    }

    private void initChatMessagesTable() {
        try {
            try {
                dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName("ChatMessages").build());
                log.info("DynamoDB table 'ChatMessages' already exists.");
            } catch (ResourceNotFoundException e) {
                log.info("Creating DynamoDB table 'ChatMessages'...");
                dynamoDbClient.createTable(CreateTableRequest.builder()
                        .tableName("ChatMessages")
                        .keySchema(
                                KeySchemaElement.builder().attributeName("PK").keyType(KeyType.HASH).build(),
                                KeySchemaElement.builder().attributeName("SK").keyType(KeyType.RANGE).build()
                        )
                        .attributeDefinitions(
                                AttributeDefinition.builder().attributeName("PK").attributeType(ScalarAttributeType.S).build(),
                                AttributeDefinition.builder().attributeName("SK").attributeType(ScalarAttributeType.S).build()
                        )
                        .billingMode(BillingMode.PAY_PER_REQUEST)
                        .build()
                );
                log.info("DynamoDB table 'ChatMessages' created successfully.");
            }
        } catch (Exception e) {
            log.warn("Could not check/create 'ChatMessages' table: {}", e.getMessage());
        }
    }
}
