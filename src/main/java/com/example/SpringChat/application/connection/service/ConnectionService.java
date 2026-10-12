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
import com.example.SpringChat.application.user.responseDTO.UserDataResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.exception.ConnectionsNotFoundException;
import com.example.SpringChat.core.connection.exception.DuplicatedUsersIdConnectionException;
import com.example.SpringChat.core.connection.exception.ForbiddenAcceptInviteException;
import com.example.SpringChat.core.connection.exception.InvitesSolicitationsNotFoundException;
import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConnectionService {

    private final ConnectionGateway connectionGateway;
    private final UserGateway userGateway;

    public ConnectionService(ConnectionGateway connectionGateway, UserGateway userGateway) {
        this.connectionGateway = connectionGateway;
        this.userGateway = userGateway;
    }

    public SendInviteResponseDTO sendInvite(UUID requesterId, UUID receiverId) {
        if (!userGateway.userExists(receiverId)) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        if (requesterId.equals(receiverId)) {
            throw new DuplicatedUsersIdConnectionException("User can't send invite to himself");
        }

        Connection connection = connectionGateway.sendInvite(requesterId, receiverId, ConnectionStatus.PENDING);
        return new SendInviteResponseDTO(
                connection.getId().toString(),
                connection.getCreatedAt(),
                connection.getUpdatedAt()
        );
    }

    public AcceptInviteResponseDTO acceptInvite(UUID connectionId, UUID authenticatedUserId) {
        Connection connection = connectionGateway.getInviteById(connectionId, ConnectionStatus.PENDING)
                .orElseThrow(() -> new ConnectionsNotFoundException("this request doesn't exist or doesn't have the pending status"));

        if (!connection.getReceiver().getId().equals(authenticatedUserId)) {
            throw new ForbiddenAcceptInviteException("Você só pode aceitar convites que foram enviados para você");
        }

        Connection accepted = connectionGateway.acceptInvite(connectionId);
        return new AcceptInviteResponseDTO(
                accepted.getId().toString(),
                accepted.getCreatedAt(),
                accepted.getUpdatedAt(),
                accepted.getConnectionStatus()
        );
    }

    public void cancelConnection(UUID connectionId) {
        if (!connectionGateway.connectionExists(connectionId)) {
            throw new ConnectionsNotFoundException("this connections doesn't exists");
        }
        connectionGateway.cancelConnection(connectionId);
    }

    public PaginationResponseDTO<InvitesSentResponseDTO> getInvitesSent(UUID userId, PaginationRequest paginationRequest) {
        PaginationResponseDTO<Connection> pendingSentConnections = connectionGateway.getInvitesSentByUser(
                userId,
                paginationRequest,
                ConnectionStatus.PENDING
        );

        if (pendingSentConnections.items().isEmpty()) {
            throw new InvitesSolicitationsNotFoundException("Não existe nenhum convite pendente enviado por você");
        }

        return pendingSentConnections.map(c -> new InvitesSentResponseDTO(
                c.getId(),
                c.getConnectionStatus(),
                toUserData(c.getReceiver())
        ));
    }

    public PaginationResponseDTO<InvitesReceivedResponseDTO> getInvitesReceived(UUID userId, PaginationRequest paginationRequest) {
        PaginationResponseDTO<Connection> pendingReceivedConnections = connectionGateway.getInvitesReceivedByUser(
                userId,
                paginationRequest,
                ConnectionStatus.PENDING
        );

        if (pendingReceivedConnections.items().isEmpty()) {
            throw new InvitesSolicitationsNotFoundException("Não existe nenhum convite pendente para você");
        }

        return pendingReceivedConnections.map(c -> new InvitesReceivedResponseDTO(
                c.getId(),
                c.getConnectionStatus(),
                toUserData(c.getRequester())
        ));
    }

    public PaginationResponseDTO<UserFriendsResponseDTO> getFriends(UUID userId, PaginationRequest paginationRequest) {
        PaginationResponseDTO<Connection> connections = connectionGateway.searchUsers(userId, paginationRequest);

        if (connections.items().isEmpty()) {
            throw new ConnectionsNotFoundException("This user didn't make any connections yet");
        }

        return connections.map(c -> {
            User friend = c.getRequester().getId().equals(userId) ? c.getReceiver() : c.getRequester();
            return new UserFriendsResponseDTO(
                    c.getId(),
                    c.getConnectionStatus(),
                    toUserData(friend)
            );
        });
    }

    private UserDataResponseDTO toUserData(User user) {
        if (user == null) {
            return null;
        }
        return new UserDataResponseDTO(
                user.getId(),
                user.getNickname(),
                user.getPublicIdentificationKey(),
                user.getUserQuote()
        );
    }
}
