package com.example.SpringChat.infrastructure.userConnection.adapter.controller;

import com.example.SpringChat.application.connection.responseDTO.AcceptInviteResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.SendInviteResponseDTO;
import com.example.SpringChat.application.connection.service.ConnectionService;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("connections")
@Tag(name = "Conexões - Ações", description = "Endpoints para envio, aceite e cancelamento de convites/conexões de amizade")
@SecurityRequirement(name = "bearerAuth")
public class ConnectionActionController {

    private final ConnectionService connectionService;

    public ConnectionActionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/send-invite/{receiverId}")
    @Operation(summary = "Enviar convite de amizade", description = "Envia uma solicitação de conexão/amizade para o usuário informado por seu UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Convite enviado com sucesso",
                    content = @Content(schema = @Schema(implementation = SendInviteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Operação inválida (ex: enviar convite para si mesmo)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário destinatário não encontrado", content = @Content)
    })
    public ResponseEntity<SendInviteResponseDTO> sendInvite(
            @Parameter(description = "UUID do usuário destinatário do convite", required = true)
            @PathVariable UUID receiverId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserEntity requester
    ) {
        SendInviteResponseDTO response = connectionService.sendInvite(requester.getId(), receiverId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/accept-invite/{id}")
    @Operation(summary = "Aceitar convite de amizade", description = "Aceita um convite de amizade recebido através do ID da solicitação de conexão.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Convite aceito com sucesso",
                    content = @Content(schema = @Schema(implementation = AcceptInviteResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Proibido: O usuário não é o destinatário deste convite", content = @Content),
            @ApiResponse(responseCode = "404", description = "Solicitação de conexão não encontrada", content = @Content)
    })
    public ResponseEntity<AcceptInviteResponseDTO> acceptInvite(
            @Parameter(description = "UUID da conexão/convite a ser aceito", required = true)
            @PathVariable UUID id,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserEntity user
    ) {
        AcceptInviteResponseDTO response = connectionService.acceptInvite(id, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/cancel-connection/{id}")
    @Operation(summary = "Cancelar convite ou desfazer amizade", description = "Cancela um convite enviado/pendente ou desfaz uma conexão de amizade existente pelo ID da conexão.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conexão cancelada/desfeita com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Conexão não encontrada", content = @Content)
    })
    public ResponseEntity<Void> cancelInvite(
            @Parameter(description = "UUID da conexão a ser cancelada", required = true)
            @PathVariable UUID id
    ) {
        connectionService.cancelConnection(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
