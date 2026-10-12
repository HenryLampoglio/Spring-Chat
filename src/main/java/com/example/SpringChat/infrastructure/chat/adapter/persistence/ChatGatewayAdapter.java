package com.example.SpringChat.infrastructure.chat.adapter.persistence;

import com.example.SpringChat.application.chat.gateway.ChatGateway;
import com.example.SpringChat.core.chat.entity.Chat;
import com.example.SpringChat.core.chat.entity.ChatMetadata;
import com.example.SpringChat.core.chat.entity.UserChatLink;
import com.example.SpringChat.core.enums.ChatAccessType;
import com.example.SpringChat.core.enums.ChatType;
import com.example.SpringChat.core.user.exception.UserNotFoundException;
import com.example.SpringChat.infrastructure.user.persistence.repository.SpringUserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.Instant;
import java.util.*;

@Component
public class ChatGatewayAdapter implements ChatGateway {

    private static final String TABLE_NAME = "Chat_System";
    private final DynamoDbClient dynamoDbClient;
    private final SpringUserRepository springUserRepository;

    public ChatGatewayAdapter(
            DynamoDbClient dynamoDbClient,
            SpringUserRepository springUserRepository
    ) {
        this.dynamoDbClient = dynamoDbClient;
        this.springUserRepository = springUserRepository;
    }

    @Override
    public Optional<Chat> findPersonalChatByUsers(UUID userIdA, UUID userIdB) {
        // 1. O(1) Fast path: busca direta pelo índice determinístico
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("pk", AttributeValue.builder().s("USER#" + userIdA).build());
        key.put("sk", AttributeValue.builder().s("DIRECT#" + userIdB).build());

        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build());

        if (response.hasItem() && response.item().containsKey("chatId")) {
            UUID chatId = UUID.fromString(response.item().get("chatId").s());
            Chat chat = new Chat(ChatType.PERSONAL, null, ChatAccessType.PRIVATE, null, userIdA);
            chat.setId(chatId);
            return Optional.of(chat);
        }

        // 2. Fallback de retrocompatibilidade para chats legados
        List<UserChatLink> userAChats = findUserChatsFromTable(userIdA.toString());
        for (UserChatLink link : userAChats) {
            if ("PERSONAL".equals(link.getRole())) {
                Map<String, AttributeValue> userBKey = new HashMap<>();
                userBKey.put("pk", AttributeValue.builder().s("USER#" + userIdB).build());
                userBKey.put("sk", AttributeValue.builder().s("CHAT#" + link.getChatId()).build());

                GetItemResponse userBResponse = dynamoDbClient.getItem(GetItemRequest.builder()
                        .tableName(TABLE_NAME)
                        .key(userBKey)
                        .build());

                if (userBResponse.hasItem()) {
                    Chat chat = new Chat(ChatType.PERSONAL, null, ChatAccessType.PRIVATE, null, userIdA);
                    chat.setId(UUID.fromString(link.getChatId()));
                    return Optional.of(chat);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Chat createPersonalChat(UUID userIdA, UUID userIdB) {
        springUserRepository.findById(userIdA)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userIdA));

        springUserRepository.findById(userIdB)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userIdB));

        UUID chatId = UUID.randomUUID();
        String chatIdStr = chatId.toString();
        String now = Instant.now().toString();

        // 1. Mapeamento direto O(1) User A -> User B
        Map<String, AttributeValue> directA = new HashMap<>();
        directA.put("pk", AttributeValue.builder().s("USER#" + userIdA).build());
        directA.put("sk", AttributeValue.builder().s("DIRECT#" + userIdB).build());
        directA.put("chatId", AttributeValue.builder().s(chatIdStr).build());
        directA.put("role", AttributeValue.builder().s("PERSONAL").build());

        // 2. Mapeamento direto O(1) User B -> User A
        Map<String, AttributeValue> directB = new HashMap<>();
        directB.put("pk", AttributeValue.builder().s("USER#" + userIdB).build());
        directB.put("sk", AttributeValue.builder().s("DIRECT#" + userIdA).build());
        directB.put("chatId", AttributeValue.builder().s(chatIdStr).build());
        directB.put("role", AttributeValue.builder().s("PERSONAL").build());

        // 3. Vínculo User A no Chat (GSI Sidebar)
        Map<String, AttributeValue> linkA = new HashMap<>();
        linkA.put("pk", AttributeValue.builder().s("USER#" + userIdA).build());
        linkA.put("sk", AttributeValue.builder().s("CHAT#" + chatIdStr).build());
        linkA.put("chatId", AttributeValue.builder().s(chatIdStr).build());
        linkA.put("targetUserId", AttributeValue.builder().s(userIdB.toString()).build());
        linkA.put("role", AttributeValue.builder().s("PERSONAL").build());
        linkA.put("permissions", AttributeValue.builder().ss(List.of("READ", "WRITE")).build());
        linkA.put("lastMessageContent", AttributeValue.builder().s("").build());
        linkA.put("lastMessageSentAt", AttributeValue.builder().s(now).build());
        linkA.put("senderId", AttributeValue.builder().s("").build());

        // 4. Vínculo User B no Chat (GSI Sidebar)
        Map<String, AttributeValue> linkB = new HashMap<>();
        linkB.put("pk", AttributeValue.builder().s("USER#" + userIdB).build());
        linkB.put("sk", AttributeValue.builder().s("CHAT#" + chatIdStr).build());
        linkB.put("chatId", AttributeValue.builder().s(chatIdStr).build());
        linkB.put("targetUserId", AttributeValue.builder().s(userIdA.toString()).build());
        linkB.put("role", AttributeValue.builder().s("PERSONAL").build());
        linkB.put("permissions", AttributeValue.builder().ss(List.of("READ", "WRITE")).build());
        linkB.put("lastMessageContent", AttributeValue.builder().s("").build());
        linkB.put("lastMessageSentAt", AttributeValue.builder().s(now).build());
        linkB.put("senderId", AttributeValue.builder().s("").build());

        // 5. Metadados centrais do Chat
        Map<String, AttributeValue> metadata = new HashMap<>();
        metadata.put("pk", AttributeValue.builder().s("METADATA").build());
        metadata.put("sk", AttributeValue.builder().s("CHAT#" + chatIdStr).build());
        metadata.put("chatId", AttributeValue.builder().s(chatIdStr).build());
        metadata.put("participantIds", AttributeValue.builder().ss(List.of(userIdA.toString(), userIdB.toString())).build());
        metadata.put("lastMessageContent", AttributeValue.builder().s("").build());
        metadata.put("lastMessageSentAt", AttributeValue.builder().s(now).build());
        metadata.put("senderId", AttributeValue.builder().s("").build());

        // TransactWriteItems atômico direto via DynamoDbClient (livre de reflexão e ClassCastException do DevTools)
        TransactWriteItemsRequest transactRequest = TransactWriteItemsRequest.builder()
                .transactItems(
                        TransactWriteItem.builder().put(Put.builder().tableName(TABLE_NAME).item(directA).build()).build(),
                        TransactWriteItem.builder().put(Put.builder().tableName(TABLE_NAME).item(directB).build()).build(),
                        TransactWriteItem.builder().put(Put.builder().tableName(TABLE_NAME).item(linkA).build()).build(),
                        TransactWriteItem.builder().put(Put.builder().tableName(TABLE_NAME).item(linkB).build()).build(),
                        TransactWriteItem.builder().put(Put.builder().tableName(TABLE_NAME).item(metadata).build()).build()
                )
                .build();

        dynamoDbClient.transactWriteItems(transactRequest);

        Chat chat = new Chat(ChatType.PERSONAL, null, ChatAccessType.PRIVATE, null, userIdA);
        chat.setId(chatId);
        return chat;
    }

    @Override
    public Boolean verifyUserChatAccess(String userId, String chatId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("pk", AttributeValue.builder().s("USER#" + userId).build());
        key.put("sk", AttributeValue.builder().s("CHAT#" + chatId).build());

        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build());

        return response.hasItem();
    }

    @Override
    public String generateNewMessageUlid() {
        return UlidCreator.getMonotonicUlid().toString();
    }

    @Override
    public List<UserChatLink> findUserChats(String userId) {
        // Tenta usar o GSI_User_Sidebar ordenado decrescentemente
        try {
            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":pk", AttributeValue.builder().s("USER#" + userId).build());

            QueryRequest queryRequest = QueryRequest.builder()
                    .tableName(TABLE_NAME)
                    .indexName("GSI_User_Sidebar")
                    .keyConditionExpression("pk = :pk")
                    .expressionAttributeValues(expressionValues)
                    .scanIndexForward(false)
                    .limit(50)
                    .build();

            QueryResponse queryResponse = dynamoDbClient.query(queryRequest);
            if (queryResponse.hasItems() && !queryResponse.items().isEmpty()) {
                return queryResponse.items().stream()
                        .map(item -> mapItemToUserChatLink(userId, item))
                        .toList();
            }
        } catch (Exception ignored) {
            // Se o GSI ainda não foi criado ou falhar, executa fallback na tabela principal
        }

        return findUserChatsFromTable(userId);
    }

    private List<UserChatLink> findUserChatsFromTable(String userId) {
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":pk", AttributeValue.builder().s("USER#" + userId).build());
        expressionValues.put(":skPrefix", AttributeValue.builder().s("CHAT#").build());

        QueryRequest fallbackRequest = QueryRequest.builder()
                .tableName(TABLE_NAME)
                .keyConditionExpression("pk = :pk and begins_with(sk, :skPrefix)")
                .expressionAttributeValues(expressionValues)
                .build();

        QueryResponse fallbackResponse = dynamoDbClient.query(fallbackRequest);
        return fallbackResponse.items().stream()
                .map(item -> mapItemToUserChatLink(userId, item))
                .toList();
    }

    private UserChatLink mapItemToUserChatLink(String userId, Map<String, AttributeValue> item) {
        String chatId = item.containsKey("chatId") ? item.get("chatId").s() : item.get("sk").s().replace("CHAT#", "");
        String chatName = item.containsKey("chatName") ? item.get("chatName").s() : null;
        String role = item.containsKey("role") ? item.get("role").s() : null;
        List<String> permissions = item.containsKey("permissions") && item.get("permissions").hasSs()
                ? item.get("permissions").ss()
                : Collections.emptyList();
        String targetUserId = item.containsKey("targetUserId") ? item.get("targetUserId").s() : null;
        String lastMessageContent = item.containsKey("lastMessageContent") ? item.get("lastMessageContent").s() : null;
        String lastMessageSentAt = item.containsKey("lastMessageSentAt") ? item.get("lastMessageSentAt").s() : null;
        String senderId = item.containsKey("senderId") ? item.get("senderId").s() : null;

        return new UserChatLink(
                userId,
                chatId,
                chatName,
                role,
                permissions,
                targetUserId,
                lastMessageContent,
                lastMessageSentAt,
                senderId
        );
    }

    @Override
    public List<ChatMetadata> findChatsMetadata(List<String> chatIds) {
        if (chatIds == null || chatIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<ChatMetadata> allMetadata = new ArrayList<>();
        for (int i = 0; i < chatIds.size(); i += 100) {
            List<String> chunk = chatIds.subList(i, Math.min(i + 100, chatIds.size()));
            List<Map<String, AttributeValue>> keys = chunk.stream().map(chatId -> {
                Map<String, AttributeValue> k = new HashMap<>();
                k.put("pk", AttributeValue.builder().s("METADATA").build());
                k.put("sk", AttributeValue.builder().s("CHAT#" + chatId).build());
                return k;
            }).toList();

            BatchGetItemRequest batchRequest = BatchGetItemRequest.builder()
                    .requestItems(Map.of(TABLE_NAME, KeysAndAttributes.builder().keys(keys).build()))
                    .build();

            BatchGetItemResponse response = dynamoDbClient.batchGetItem(batchRequest);
            List<Map<String, AttributeValue>> items = response.responses().getOrDefault(TABLE_NAME, Collections.emptyList());
            for (Map<String, AttributeValue> item : items) {
                allMetadata.add(new ChatMetadata(
                        item.get("sk").s().replace("CHAT#", ""),
                        item.containsKey("lastMessageContent") ? item.get("lastMessageContent").s() : "",
                        item.containsKey("lastMessageSentAt") ? item.get("lastMessageSentAt").s() : "",
                        item.containsKey("senderId") ? item.get("senderId").s() : ""
                ));
            }
        }
        return allMetadata;
    }

    @Override
    public void updateLastMessage(String chatId, String content, String sentAt, String senderId) {
        // 1. Busca metadados para saber os participantes
        Map<String, AttributeValue> metaKey = new HashMap<>();
        metaKey.put("pk", AttributeValue.builder().s("METADATA").build());
        metaKey.put("sk", AttributeValue.builder().s("CHAT#" + chatId).build());

        GetItemResponse metaResponse = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(metaKey)
                .build());

        List<String> participants = Collections.emptyList();
        if (metaResponse.hasItem() && metaResponse.item().containsKey("participantIds") && metaResponse.item().get("participantIds").hasSs()) {
            participants = metaResponse.item().get("participantIds").ss();
        }

        // 2. Atualiza METADATA
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":content", AttributeValue.builder().s(content).build());
        expressionAttributeValues.put(":sentAt", AttributeValue.builder().s(sentAt).build());
        expressionAttributeValues.put(":senderId", AttributeValue.builder().s(senderId).build());

        UpdateItemRequest metaRequest = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(metaKey)
                .updateExpression("SET lastMessageContent = :content, lastMessageSentAt = :sentAt, senderId = :senderId")
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        dynamoDbClient.updateItem(metaRequest);

        // 3. Atualiza os itens dos participantes (para alimentar o GSI_User_Sidebar)
        for (String participantId : participants) {
            Map<String, AttributeValue> userKey = new HashMap<>();
            userKey.put("pk", AttributeValue.builder().s("USER#" + participantId).build());
            userKey.put("sk", AttributeValue.builder().s("CHAT#" + chatId).build());

            UpdateItemRequest userRequest = UpdateItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(userKey)
                    .updateExpression("SET lastMessageContent = :content, lastMessageSentAt = :sentAt, senderId = :senderId")
                    .expressionAttributeValues(expressionAttributeValues)
                    .build();

            try {
                dynamoDbClient.updateItem(userRequest);
            } catch (Exception ignored) {}
        }
    }
}