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
import com.example.SpringChat.application.shared.response.PaginationResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.presenter.ConnectionInfoPresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "Conexões - Informações", description = "Endpoints para consulta de convites enviados, convites recebidos e lista de amigos")
@SecurityRequirement(name = "bearerAuth")
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
    @Operation(summary = "Listar convites de amizade enviados", description = "Retorna uma lista paginada dos convites de amizade enviados pelo usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de convites enviados obtida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    public ResponseEntity<PaginationResponseDTO<InvitesSentResponseDTO>> invitesSent(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserEntity user,
            @ParameterObject
            @PageableDefault(page = 0, size = 10) Pageable pageable
    )
    {
        PaginationRequest paginationRequest = new PaginationRequest(pageable.getPageNumber(), pageable.getPageSize());
        InvitesSentCommand command = new InvitesSentCommand(user.getId(), paginationRequest);
        PaginationResponseDTO<Connection> foundInvites = invitesSentInputPort.execute(command);
        PaginationResponseDTO<InvitesSentResponseDTO> response = foundInvites.map(connectionInfoPresenter::toInvitesSentResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/invites-received")
    @Operation(summary = "Listar convites de amizade recebidos", description = "Retorna uma lista paginada dos convites de amizade recebidos pelo usuário autenticado pendentes de resposta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de convites recebidos obtida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    public ResponseEntity<PaginationResponseDTO<InvitesReceivedResponseDTO>> invitesReceived(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserEntity user,
            @ParameterObject
            @PageableDefault(page = 0, size = 10) Pageable pageable
    )
    {
        PaginationRequest paginationRequest = new PaginationRequest(pageable.getPageNumber(), pageable.getPageSize());
        InvitesReceivedCommand command = new InvitesReceivedCommand(user.getId(), paginationRequest);
        PaginationResponseDTO<Connection> foundInvites = invitesReceivedInputPort.execute(command);
        PaginationResponseDTO<InvitesReceivedResponseDTO> response = foundInvites.map(connectionInfoPresenter::toInviteReceivedResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/friends")
    @Operation(summary = "Listar amigos (conexões aceitas)", description = "Retorna uma lista paginada de todos os amigos conectados com o usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de amigos obtida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    public ResponseEntity<PaginationResponseDTO<UserFriendsResponseDTO>> userFriends(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserEntity user,
            @ParameterObject
            @PageableDefault(page = 0, size = 10) Pageable pageable
    )
    {
        PaginationRequest paginationRequest = new PaginationRequest(pageable.getPageNumber(), pageable.getPageSize());
        UserFriendsCommand command = new UserFriendsCommand(user.getId(), paginationRequest);
        PaginationResponseDTO<Connection> foundConnections = userFriendsInputPort.execute(command);
        PaginationResponseDTO<UserFriendsResponseDTO> response = foundConnections
                .map(connection -> this.connectionInfoPresenter.toUserFriendsResponse(connection, user.getId()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
