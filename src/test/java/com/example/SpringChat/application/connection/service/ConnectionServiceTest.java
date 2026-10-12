package com.example.SpringChat.application.connection.service;

import com.example.SpringChat.application.connection.gateway.ConnectionGateway;
import com.example.SpringChat.application.connection.responseDTO.AcceptInviteResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.InvitesReceivedResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.InvitesSentResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.SendInviteResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.UserFriendsResponseDTO;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.application.shared.response.PaginationResponseDTO;
import com.example.SpringChat.application.user.gateway.UserGateway;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.exception.ConnectionsNotFoundException;
import com.example.SpringChat.core.connection.exception.DuplicatedUsersIdConnectionException;
import com.example.SpringChat.core.connection.exception.ForbiddenAcceptInviteException;
import com.example.SpringChat.core.connection.exception.InvitesSolicitationsNotFoundException;
import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

    @Mock
    private ConnectionGateway connectionGateway;

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private ConnectionService connectionService;

    private UUID userAId;
    private UUID userBId;
    private User userA;
    private User userB;
    private Connection connection;

    @BeforeEach
    void setUp() {
        userAId = UUID.randomUUID();
        userBId = UUID.randomUUID();

        userA = new User("UserA", "usera@mail.com", "hashA", 111111);
        userA.setId(userAId);

        userB = new User("UserB", "userb@mail.com", "hashB", 222222);
        userB.setId(userBId);

        connection = new Connection();
        connection.setId(UUID.randomUUID());
        connection.setRequester(userA);
        connection.setReceiver(userB);
        connection.setConnectionStatus(ConnectionStatus.PENDING);
        connection.setCreatedAt(LocalDateTime.now());
        connection.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void shouldSendInviteSuccessfully() {
        Mockito.when(userGateway.userExists(userBId)).thenReturn(true);
        Mockito.when(connectionGateway.sendInvite(userAId, userBId, ConnectionStatus.PENDING)).thenReturn(connection);

        SendInviteResponseDTO response = connectionService.sendInvite(userAId, userBId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(connection.getId().toString(), response.connectionId());
        Mockito.verify(connectionGateway, Mockito.times(1)).sendInvite(userAId, userBId, ConnectionStatus.PENDING);
    }

    @Test
    void shouldThrowWhenReceiverNotFoundOnSendInvite() {
        Mockito.when(userGateway.userExists(userBId)).thenReturn(false);

        Assertions.assertThrows(UserNotFoundException.class, () ->
                connectionService.sendInvite(userAId, userBId)
        );
        Mockito.verify(connectionGateway, Mockito.never()).sendInvite(any(), any(), any());
    }

    @Test
    void shouldThrowWhenSendingInviteToSelf() {
        Mockito.when(userGateway.userExists(userAId)).thenReturn(true);

        Assertions.assertThrows(DuplicatedUsersIdConnectionException.class, () ->
                connectionService.sendInvite(userAId, userAId)
        );
        Mockito.verify(connectionGateway, Mockito.never()).sendInvite(any(), any(), any());
    }

    @Test
    void shouldAcceptInviteSuccessfully() {
        Mockito.when(connectionGateway.getInviteById(connection.getId(), ConnectionStatus.PENDING))
                .thenReturn(Optional.of(connection));
        connection.setConnectionStatus(ConnectionStatus.ACCEPTED);
        Mockito.when(connectionGateway.acceptInvite(connection.getId())).thenReturn(connection);

        AcceptInviteResponseDTO response = connectionService.acceptInvite(connection.getId(), userBId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(ConnectionStatus.ACCEPTED, response.connectionStatus());
        Mockito.verify(connectionGateway, Mockito.times(1)).acceptInvite(connection.getId());
    }

    @Test
    void shouldThrowWhenAcceptInviteNotFound() {
        Mockito.when(connectionGateway.getInviteById(connection.getId(), ConnectionStatus.PENDING))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ConnectionsNotFoundException.class, () ->
                connectionService.acceptInvite(connection.getId(), userBId)
        );
    }

    @Test
    void shouldThrowWhenAcceptInviteFromNonReceiver() {
        Mockito.when(connectionGateway.getInviteById(connection.getId(), ConnectionStatus.PENDING))
                .thenReturn(Optional.of(connection));

        Assertions.assertThrows(ForbiddenAcceptInviteException.class, () ->
                connectionService.acceptInvite(connection.getId(), userAId)
        );
    }

    @Test
    void shouldCancelConnectionSuccessfully() {
        Mockito.when(connectionGateway.connectionExists(connection.getId())).thenReturn(true);

        connectionService.cancelConnection(connection.getId());

        Mockito.verify(connectionGateway, Mockito.times(1)).cancelConnection(connection.getId());
    }

    @Test
    void shouldThrowWhenCancelConnectionNotFound() {
        Mockito.when(connectionGateway.connectionExists(connection.getId())).thenReturn(false);

        Assertions.assertThrows(ConnectionsNotFoundException.class, () ->
                connectionService.cancelConnection(connection.getId())
        );
    }

    @Test
    void shouldGetInvitesSentSuccessfully() {
        PaginationRequest paginationRequest = new PaginationRequest(0, 10);
        PaginationResponseDTO<Connection> page = new PaginationResponseDTO<>(List.of(connection), 0, 1);
        Mockito.when(connectionGateway.getInvitesSentByUser(userAId, paginationRequest, ConnectionStatus.PENDING))
                .thenReturn(page);

        PaginationResponseDTO<InvitesSentResponseDTO> response = connectionService.getInvitesSent(userAId, paginationRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.items().size());
        Assertions.assertEquals("UserB", response.items().get(0).userData().nickname());
    }

    @Test
    void shouldThrowWhenNoInvitesSent() {
        PaginationRequest paginationRequest = new PaginationRequest(0, 10);
        PaginationResponseDTO<Connection> emptyPage = new PaginationResponseDTO<>(Collections.emptyList(), 0, 0);
        Mockito.when(connectionGateway.getInvitesSentByUser(userAId, paginationRequest, ConnectionStatus.PENDING))
                .thenReturn(emptyPage);

        Assertions.assertThrows(InvitesSolicitationsNotFoundException.class, () ->
                connectionService.getInvitesSent(userAId, paginationRequest)
        );
    }

    @Test
    void shouldGetInvitesReceivedSuccessfully() {
        PaginationRequest paginationRequest = new PaginationRequest(0, 10);
        PaginationResponseDTO<Connection> page = new PaginationResponseDTO<>(List.of(connection), 0, 1);
        Mockito.when(connectionGateway.getInvitesReceivedByUser(userBId, paginationRequest, ConnectionStatus.PENDING))
                .thenReturn(page);

        PaginationResponseDTO<InvitesReceivedResponseDTO> response = connectionService.getInvitesReceived(userBId, paginationRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.items().size());
        Assertions.assertEquals("UserA", response.items().get(0).userData().nickname());
    }

    @Test
    void shouldThrowWhenNoInvitesReceived() {
        PaginationRequest paginationRequest = new PaginationRequest(0, 10);
        PaginationResponseDTO<Connection> emptyPage = new PaginationResponseDTO<>(Collections.emptyList(), 0, 0);
        Mockito.when(connectionGateway.getInvitesReceivedByUser(userBId, paginationRequest, ConnectionStatus.PENDING))
                .thenReturn(emptyPage);

        Assertions.assertThrows(InvitesSolicitationsNotFoundException.class, () ->
                connectionService.getInvitesReceived(userBId, paginationRequest)
        );
    }

    @Test
    void shouldGetFriendsSuccessfully() {
        PaginationRequest paginationRequest = new PaginationRequest(0, 10);
        connection.setConnectionStatus(ConnectionStatus.ACCEPTED);
        PaginationResponseDTO<Connection> page = new PaginationResponseDTO<>(List.of(connection), 0, 1);
        Mockito.when(connectionGateway.searchUsers(userAId, paginationRequest)).thenReturn(page);

        PaginationResponseDTO<UserFriendsResponseDTO> response = connectionService.getFriends(userAId, paginationRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.items().size());
        Assertions.assertEquals("UserB", response.items().get(0).userData().nickname());
    }

    @Test
    void shouldThrowWhenNoFriendsFound() {
        PaginationRequest paginationRequest = new PaginationRequest(0, 10);
        PaginationResponseDTO<Connection> emptyPage = new PaginationResponseDTO<>(Collections.emptyList(), 0, 0);
        Mockito.when(connectionGateway.searchUsers(userAId, paginationRequest)).thenReturn(emptyPage);

        Assertions.assertThrows(ConnectionsNotFoundException.class, () ->
                connectionService.getFriends(userAId, paginationRequest)
        );
    }
}
