package com.example.SpringChat.infrastructure.userConnection.adapter.controller;


import com.example.SpringChat.application.connection.command.*;
import com.example.SpringChat.application.connection.port.*;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.acceptInvite.response.AcceptInviteResponse;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.cancelConnection.response.CancelConnectionResponse;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.invitesReceived.response.InvitesReceivedResponse;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.invitesSent.response.InvitesSentResponse;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.sendInvite.response.SendInviteResponse;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.userFriends.response.UserFriendsResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("connections")
public class ConnectionController {
    private final UserFriendsInputPort userFriendsInputPort;
    private final SendInvitePort sendInvitePort;
    private final CancelConnectionPort cancelConnectionPort;
    private final AcceptInvitePort acceptInvitePort;
    private final InvitesSentInputPort invitesSentInputPort;
    private final InvitesReceivedInputPort invitesReceivedInputPort;

    public ConnectionController(
            UserFriendsInputPort userFriendsInputPort,
            SendInvitePort sendInvitePort,
            CancelConnectionPort cancelConnectionPort,
            AcceptInvitePort acceptInvitePort,
            InvitesSentInputPort invitesSentInputPort,
            InvitesReceivedInputPort invitesReceivedInputPort
    ){
        this.userFriendsInputPort = userFriendsInputPort;
        this.sendInvitePort = sendInvitePort;
        this.cancelConnectionPort = cancelConnectionPort;
        this.acceptInvitePort = acceptInvitePort;
        this.invitesSentInputPort = invitesSentInputPort;
        this.invitesReceivedInputPort = invitesReceivedInputPort;
    }

    @GetMapping("/invites-sent")
    public ResponseEntity<InvitesSentResponse> invitesSent(
            @AuthenticationPrincipal UserEntity user,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    )
    {
        PaginationRequest paginationRequest = new PaginationRequest(pageable.getPageNumber(), pageable.getPageSize());
        InvitesSentCommand command = new InvitesSentCommand(user.getId(), paginationRequest);
        PaginationResponse<Connection> foundInvites = invitesSentInputPort.execute(command);
        InvitesSentResponse response = new InvitesSentResponse(
            foundInvites
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/invites-received")
    public ResponseEntity<InvitesReceivedResponse> invitesReceived(
            @AuthenticationPrincipal UserEntity user,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    )
    {
        PaginationRequest paginationRequest = new PaginationRequest(pageable.getPageNumber(), pageable.getPageSize());
        InvitesReceivedCommand command = new InvitesReceivedCommand(user.getId(), paginationRequest);
        PaginationResponse<Connection> foundInvites = invitesReceivedInputPort.execute(command);
        InvitesReceivedResponse response = new InvitesReceivedResponse(
                foundInvites
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/friends")
    public ResponseEntity<UserFriendsResponse> userFriends(
            @AuthenticationPrincipal UserEntity user,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    )
    {
        PaginationRequest paginationRequest = new PaginationRequest(pageable.getPageNumber(), pageable.getPageSize());
        UserFriendsCommand command = new UserFriendsCommand(user.getId(), paginationRequest);
        PaginationResponse<Connection> foundConnections = userFriendsInputPort.execute(command);
        UserFriendsResponse response = new UserFriendsResponse(
            "All user connections retrieved",
            foundConnections
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/send-invite/{receiverId}")
    public ResponseEntity<SendInviteResponse> sendInvite(
    @PathVariable UUID receiverId,
    @AuthenticationPrincipal UserEntity requester
    )
    {
        SendInviteCommand command = new SendInviteCommand(requester.getId(), receiverId);
        Connection connection = sendInvitePort.execute(command);
        SendInviteResponse response = new SendInviteResponse(
            "Convite Enviado",
            connection
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/accept-invite/{id}")
    public ResponseEntity<AcceptInviteResponse> acceptInvite(
            @PathVariable UUID id
    )
    {
        AcceptInviteCommand command = new AcceptInviteCommand(id);
        Connection connection = acceptInvitePort.execute(command);
        AcceptInviteResponse response = new AcceptInviteResponse(
                connection,
                "Convite aceito"
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/cancel-connection/{id}")
    public ResponseEntity<CancelConnectionResponse> cancelInvite(
        @PathVariable UUID id
    )
    {
        CancelConnectionCommand command = new CancelConnectionCommand(id);
        cancelConnectionPort.execute(command);

        return ResponseEntity.ok(new CancelConnectionResponse("Convite cancelado com sucesso.",true));
    }
}
