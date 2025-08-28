package com.example.SpringChat.infrastructure.user.adapter.controller;

import com.example.SpringChat.application.user.command.CreateUserCommand;
import com.example.SpringChat.application.user.port.CreateUserInputPort;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.CreateUserRequest;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserInputPort createUserInputPort;

    public UserController(CreateUserInputPort createUserInputPort){
        this.createUserInputPort = createUserInputPort;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request){
        CreateUserCommand command = new CreateUserCommand(
                request.getNickname(),
                request.getEmail(),
                request.getHashedPassword()
        );
        User createdUser = createUserInputPort.execute(command);
        UserResponse response = new UserResponse(
                createdUser.getId(),
                createdUser.getNickname(),
                createdUser.getEmail(),
                createdUser.getPublicIdentificationKey()
        );

        // Corrected line
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}