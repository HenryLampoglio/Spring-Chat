package com.example.SpringChat.infrastructure.userConnection.adapter.controller;

import com.example.SpringChat.application.connection.command.InvitesReceivedCommand;
import com.example.SpringChat.application.connection.command.InvitesSentCommand;
import com.example.SpringChat.application.connection.command.UserFriendsCommand;
import com.example.SpringChat.application.connection.port.InvitesReceivedInputPort;
import com.example.SpringChat.application.connection.port.InvitesSentInputPort;
import com.example.SpringChat.application.connection.port.UserFriendsInputPort;
import com.example.SpringChat.application.connection.responseDTO.InvitesReceivedResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.InvitesSentResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.UserFriendsResponseDTO;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.presenter.ConnectionInfoPresenter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("connections")
public class ConnectionInfoController {
    private final UserFriendsInputPort userFriendsInputPort;
    private final InvitesSentInputPort invitesSentInputPort;
    private final InvitesReceivedInputPort invitesReceivedInputPort;
    private final ConnectionInfoPresenter connectionInfoPresenter;

    public ConnectionInfoController(
            UserFriendsInputPort userFriendsInputPort,
            InvitesSentInputPort invitesSentInputPort,
            InvitesReceivedInputPort invitesReceivedInputPort,
            ConnectionInfoPresenter connectionInfoPresenter
    ){
        this.userFriendsInputPort = userFriendsInputPort;
        this.invitesSentInputPort = invitesSentInputPort;
        this.invitesReceivedInputPort = invitesReceivedInputPort;
        this.connectionInfoPresenter = connectionInfoPresenter;
    }

    @GetMapping("/invites-sent")
    public ResponseEntity<PaginationResponse<InvitesSentResponseDTO>> invitesSent(
            @AuthenticationPrincipal UserEntity user,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    )
    {
        PaginationRequest paginationRequest = new PaginationRequest(pageable.getPageNumber(), pageable.getPageSize());
        InvitesSentCommand command = new InvitesSentCommand(user.getId(), paginationRequest);
        PaginationResponse<Connection> foundInvites = invitesSentInputPort.execute(command);
        PaginationResponse<InvitesSentResponseDTO> response = connectionInfoPresenter.toInvitesSentResponse(foundInvites);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/invites-received")
    public ResponseEntity<PaginationResponse<InvitesReceivedResponseDTO>> invitesReceived(
            @AuthenticationPrincipal UserEntity user,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    )
    {
        PaginationRequest paginationRequest = new PaginationRequest(pageable.getPageNumber(), pageable.getPageSize());
        InvitesReceivedCommand command = new InvitesReceivedCommand(user.getId(), paginationRequest);
        PaginationResponse<Connection> foundInvites = invitesReceivedInputPort.execute(command);
        PaginationResponse<InvitesReceivedResponseDTO> response = connectionInfoPresenter.toInviteReceivedResponse(foundInvites);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/friends")
    public ResponseEntity<PaginationResponse<UserFriendsResponseDTO>> userFriends(
            @AuthenticationPrincipal UserEntity user,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    )
    {
        PaginationRequest paginationRequest = new PaginationRequest(pageable.getPageNumber(), pageable.getPageSize());
        UserFriendsCommand command = new UserFriendsCommand(user.getId(), paginationRequest);
        PaginationResponse<Connection> foundConnections = userFriendsInputPort.execute(command);
        PaginationResponse<UserFriendsResponseDTO> response = connectionInfoPresenter.toUserFriendsResponse(foundConnections);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
