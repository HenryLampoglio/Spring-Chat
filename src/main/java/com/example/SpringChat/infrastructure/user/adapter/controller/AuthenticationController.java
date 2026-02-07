package com.example.SpringChat.infrastructure.user.adapter.controller;

import com.example.SpringChat.application.user.command.CreateUserCommand;
import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.application.user.port.CreateUserInputPort;
import com.example.SpringChat.application.user.port.LoginInputPort;
import com.example.SpringChat.application.user.responseDTO.CreateUserResponseDTO;
import com.example.SpringChat.application.user.responseDTO.LoginResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication.CreateUserRequest;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication.LoginRequest;
import com.example.SpringChat.infrastructure.user.adapter.controller.presenter.UserPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private final CreateUserInputPort createUserInputPort;
    private final LoginInputPort loginInputPort;
    private final UserPresenter userPresenter;

    public AuthenticationController(CreateUserInputPort createUserInputPort, LoginInputPort loginInputPort, UserPresenter userPresenter){
        this.createUserInputPort = createUserInputPort;
        this.loginInputPort = loginInputPort;
        this.userPresenter = userPresenter;
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponseDTO> createUser(@RequestBody CreateUserRequest request){
        CreateUserCommand command = new CreateUserCommand(
                request.getNickname(),
                request.getEmail(),
                request.getHashedPassword()
        );
        User createdUser = createUserInputPort.execute(command);
        CreateUserResponseDTO response = userPresenter.toCreateUserResponse(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequest request){
        LoginCommand command = new LoginCommand(
            request.getEmail(),
            request.getPassword()
        );
        LoginResponseDTO response = loginInputPort.execute(command);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
