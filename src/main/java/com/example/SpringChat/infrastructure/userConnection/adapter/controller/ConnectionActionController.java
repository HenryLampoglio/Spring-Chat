package com.example.SpringChat.infrastructure.userConnection.adapter.controller;

import com.example.SpringChat.application.connection.command.AcceptInviteCommand;
import com.example.SpringChat.application.connection.command.CancelConnectionCommand;
import com.example.SpringChat.application.connection.command.SendInviteCommand;
import com.example.SpringChat.application.connection.port.*;
import com.example.SpringChat.application.connection.responseDTO.AcceptInviteResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.SendInviteResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.presenter.ConnectionActionPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("connections")
public class ConnectionActionController {
    private final SendInvitePort sendInvitePort;
    private final CancelConnectionPort cancelConnectionPort;
    private final AcceptInvitePort acceptInvitePort;
    private final ConnectionActionPresenter connectionActionPresenter;

    public ConnectionActionController(
            SendInvitePort sendInvitePort,
            CancelConnectionPort cancelConnectionPort,
            AcceptInvitePort acceptInvitePort,
            ConnectionActionPresenter connectionActionPresenter

    ){
        this.sendInvitePort = sendInvitePort;
        this.cancelConnectionPort = cancelConnectionPort;
        this.acceptInvitePort = acceptInvitePort;
        this.connectionActionPresenter = connectionActionPresenter;
    }

    @PostMapping("/send-invite/{receiverId}")
    public ResponseEntity<SendInviteResponseDTO> sendInvite(
            @PathVariable UUID receiverId,
            @AuthenticationPrincipal UserEntity requester
    )
    {
        SendInviteCommand command = new SendInviteCommand(requester.getId(), receiverId);
        Connection connection = sendInvitePort.execute(command);
        SendInviteResponseDTO response = connectionActionPresenter.toSendInvitesResponse(connection);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/accept-invite/{id}")
    public ResponseEntity<AcceptInviteResponseDTO> acceptInvite(
            @PathVariable UUID id
    )
    {
        AcceptInviteCommand command = new AcceptInviteCommand(id);
        Connection connection = acceptInvitePort.execute(command);
        AcceptInviteResponseDTO response = connectionActionPresenter.toAcceptInvitesResponse(connection);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/cancel-connection/{id}")
    public ResponseEntity<Void> cancelInvite(
            @PathVariable UUID id
    )
    {
        CancelConnectionCommand command = new CancelConnectionCommand(id);
        cancelConnectionPort.execute(command);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
