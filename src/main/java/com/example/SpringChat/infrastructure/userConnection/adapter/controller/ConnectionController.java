package com.example.SpringChat.infrastructure.userConnection.adapter.controller;


import com.example.SpringChat.application.connection.command.UserFriendsCommand;
import com.example.SpringChat.application.connection.port.UserFriendsInputPort;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.dto.userFriends.response.UserFriendsResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("connections")
public class ConnectionController {
    private final UserFriendsInputPort userFriendsInputPort;

    public ConnectionController(UserFriendsInputPort userFriendsInputPort){
        this.userFriendsInputPort = userFriendsInputPort;
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
}
