package com.example.SpringChat.infrastructure.user.adapter.controller;

import com.example.SpringChat.application.user.responseDTO.CreateUserResponseDTO;
import com.example.SpringChat.application.user.responseDTO.LoginResponseDTO;
import com.example.SpringChat.application.user.service.UserService;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication.CreateUserRequest;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication.LoginRequest;
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

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastrar novo usuário", description = "Cria um novo usuário na aplicação com nickname, email e senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = CreateUserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito: E-mail já cadastrado", content = @Content)
    })
    public ResponseEntity<CreateUserResponseDTO> createUser(@RequestBody CreateUserRequest request) {
        CreateUserResponseDTO response = userService.register(
                request.getNickname(),
                request.getEmail(),
                request.getHashedPassword()
        );

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
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequest request) {
        LoginResponseDTO response = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
