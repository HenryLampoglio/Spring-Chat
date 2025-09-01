package com.example.SpringChat.infrastructure.user.adapter.controller;

import com.example.SpringChat.application.user.command.CreateUserCommand;
import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.application.user.port.CreateUserInputPort;
import com.example.SpringChat.application.user.port.LoginInputPort;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.CreateUserRequest;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.CreateUserResponse;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.LoginRequest;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private final CreateUserInputPort createUserInputPort;
    private final LoginInputPort loginInputPort;

    public AuthenticationController(CreateUserInputPort createUserInputPort, LoginInputPort loginInputPort){
        this.createUserInputPort = createUserInputPort;
        this.loginInputPort = loginInputPort;
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request){
        CreateUserCommand command = new CreateUserCommand(
                request.getNickname(),
                request.getEmail(),
                request.getHashedPassword()
        );
        User createdUser = createUserInputPort.execute(command);
        CreateUserResponse response = new CreateUserResponse(
                createdUser.getId(),
                createdUser.getNickname(),
                createdUser.getEmail(),
                createdUser.getPublicIdentificationKey()
        );

        // Corrected line
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        LoginCommand command = new LoginCommand(
                request.getEmail(),
                request.getPassword()
        );
        String loggedUser = loginInputPort.execute(command);
        LoginResponse response = new LoginResponse(loggedUser);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
