package com.example.SpringChat.infrastructure.userConnection.adapter.controller;


import com.example.SpringChat.application.connection.port.UserConnectionsInputPort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("connections")
public class ConnectionController {
    private final UserConnectionsInputPort userConnectionsInputPort;

    public ConnectionController(UserConnectionsInputPort userConnectionsInputPort){
        this.userConnectionsInputPort = userConnectionsInputPort;
    }

    @GetM
}
