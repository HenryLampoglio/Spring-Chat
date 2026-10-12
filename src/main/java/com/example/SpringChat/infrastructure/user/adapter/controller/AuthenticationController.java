package com.example.SpringChat.infrastructure.user.adapter.controller;

import com.example.SpringChat.application.user.command.CreateUserCommand;
import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.application.user.output.LoginOutput;
import com.example.SpringChat.application.user.port.CreateUserInputPort;
import com.example.SpringChat.application.user.port.LoginInputPort;
import com.example.SpringChat.application.user.responseDTO.CreateUserResponseDTO;
import com.example.SpringChat.application.user.responseDTO.LoginResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication.CreateUserRequest;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication.LoginRequest;
import com.example.SpringChat.infrastructure.user.adapter.controller.presenter.UserPresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@Tag(name = "Autenticação", description = "Endpoints para cadastro e login de usuários")
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
    @Operation(summary = "Cadastrar novo usuário", description = "Cria um novo usuário na aplicação com nickname, email e senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = CreateUserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito: E-mail já cadastrado", content = @Content)
    })
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
    @Operation(summary = "Realizar login de usuário", description = "Autentica o usuário com email e senha e retorna o token JWT de acesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas / Senha incorreta", content = @Content)
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequest request){
        LoginCommand command = new LoginCommand(
            request.getEmail(),
            request.getPassword()
        );
        LoginOutput loginInfo = loginInputPort.execute(command);
        LoginResponseDTO response = userPresenter.toLoginResponse(loginInfo);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
