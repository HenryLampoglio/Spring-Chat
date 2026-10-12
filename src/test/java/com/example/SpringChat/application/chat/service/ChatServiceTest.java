package com.example.SpringChat.application.chat.service;

import com.example.SpringChat.application.chat.gateway.ChatGateway;
import com.example.SpringChat.application.chat.gateway.MessageBrokerGateway;
import com.example.SpringChat.application.chat.responseDTO.ChatMessageResponseDTO;
import com.example.SpringChat.application.chat.responseDTO.SidebarItemResponseDTO;
import com.example.SpringChat.application.message.gateway.MessageDispatchGateway;
import com.example.SpringChat.application.message.gateway.MessagePersistenceGateway;
import com.example.SpringChat.application.user.gateway.UserGateway;
import com.example.SpringChat.core.chat.entity.Chat;
import com.example.SpringChat.core.chat.entity.UserChatLink;
import com.example.SpringChat.core.chat.exception.ChatAccessDeniedException;
import com.example.SpringChat.core.message.entity.Message;
import com.example.SpringChat.core.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatGateway chatGateway;

    @Mock
    private MessageBrokerGateway messageBrokerGateway;

    @Mock
    private MessageDispatchGateway messageDispatchGateway;

    @Mock
    private MessagePersistenceGateway messagePersistenceGateway;

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private ChatService chatService;

    @Test
    void shouldReturnExistingChatWhenFound() {
        UUID userA = UUID.randomUUID();
        UUID userB = UUID.randomUUID();
        Chat existing = new Chat();
        existing.setId(UUID.randomUUID());

        Mockito.when(chatGateway.findPersonalChatByUsers(userA, userB)).thenReturn(Optional.of(existing));

        Chat result = chatService.findOrCreatePersonalChat(userA, userB);

        assertNotNull(result);
        assertEquals(existing.getId(), result.getId());
        Mockito.verify(chatGateway, Mockito.never()).createPersonalChat(any(), any());
    }

    @Test
    void shouldCreateNewChatWhenNotFound() {
        UUID userA = UUID.randomUUID();
        UUID userB = UUID.randomUUID();
        Chat created = new Chat();
        created.setId(UUID.randomUUID());

        Mockito.when(chatGateway.findPersonalChatByUsers(userA, userB)).thenReturn(Optional.empty());
        Mockito.when(chatGateway.createPersonalChat(userA, userB)).thenReturn(created);

        Chat result = chatService.findOrCreatePersonalChat(userA, userB);

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
        Mockito.verify(chatGateway, Mockito.times(1)).createPersonalChat(userA, userB);
    }

    @Test
    void shouldValidateChatAccessSuccessfully() {
        Mockito.when(chatGateway.verifyUserChatAccess("u1", "c1")).thenReturn(true);

        assertDoesNotThrow(() -> chatService.validateChatAccess("u1", "c1"));
    }

    @Test
    void shouldThrowWhenChatAccessDenied() {
        Mockito.when(chatGateway.verifyUserChatAccess("u1", "c1")).thenReturn(false);

        assertThrows(ChatAccessDeniedException.class, () -> chatService.validateChatAccess("u1", "c1"));
    }

    @Test
    void shouldSendMessageSuccessfully() {
        UUID chatId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        String nickname = "john";
        String content = "Hello world!";
        String ulid = "01ARZ3NDEKTSV4RRFFQ69G5FAV";

        Mockito.when(chatGateway.generateNewMessageUlid()).thenReturn(ulid);

        chatService.sendMessage(chatId, senderId, nickname, content);

        // Verify ULID generation
        Mockito.verify(chatGateway, Mockito.times(1)).generateNewMessageUlid();

        // Verify async dispatch to RabbitMQ
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(messageDispatchGateway, Mockito.times(1)).dispatchMessage(messageCaptor.capture());
        Message dispatched = messageCaptor.getValue();
        assertEquals(ulid, dispatched.getId());
        assertEquals(chatId, dispatched.getChatId());
        assertEquals(content, dispatched.getContent());

        // Verify real-time broadcast to Redis
        Mockito.verify(messageBrokerGateway, Mockito.times(1)).publish(eq("chat." + chatId), any(ChatMessageResponseDTO.class));

        // Verify DynamoDB last message update for sidebar
        Mockito.verify(chatGateway, Mockito.times(1)).updateLastMessage(eq(chatId.toString()), eq(content), any(), eq(senderId.toString()));
    }

    @Test
    void shouldLoadSidebarWithResolvedNicknames() {
        UUID userId = UUID.randomUUID();
        UUID friendId = UUID.randomUUID();
        String chatId = UUID.randomUUID().toString();

        UserChatLink link = new UserChatLink(
                userId.toString(),
                chatId,
                null,
                "PERSONAL",
                List.of("READ", "WRITE"),
                friendId.toString(),
                "Tudo bem?",
                Instant.now().toString(),
                friendId.toString()
        );

        User friend = new User("friendUser", "friend@mail.com", "hash", 123);
        friend.setId(friendId);

        Mockito.when(chatGateway.findUserChats(userId.toString())).thenReturn(List.of(link));
        Mockito.when(userGateway.findByIds(any())).thenReturn(List.of(friend));

        List<SidebarItemResponseDTO> sidebar = chatService.loadSidebar(userId);

        assertEquals(1, sidebar.size());
        assertEquals(chatId, sidebar.get(0).chatId());
        assertEquals("friendUser", sidebar.get(0).chatName());
        assertEquals("friendUser", sidebar.get(0).senderNickname());
        assertEquals("Tudo bem?", sidebar.get(0).lastMessageContent());
    }

    @Test
    void shouldFindChatHistory() {
        UUID chatId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        Message msg1 = new Message("01ARZ3...", chatId, senderId, "sender", "msg 1", Instant.now());

        Mockito.when(messagePersistenceGateway.findMessagesByChatId(chatId, null, 20))
                .thenReturn(List.of(msg1));

        List<ChatMessageResponseDTO> history = chatService.findChatHistory(chatId, null, 20);

        assertEquals(1, history.size());
        assertEquals("01ARZ3...", history.get(0).id());
        assertEquals("msg 1", history.get(0).content());
    }
}
