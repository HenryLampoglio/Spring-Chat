package com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Requisição para criação de novo usuário")
public class CreateUserRequest {
    @Schema(description = "Apelido único do usuário", example = "joaodev")
    private String nickname;

    @Schema(description = "Endereço de e-mail do usuário", example = "joao@email.com")
    private String email;

    @Schema(description = "Senha do usuário", example = "SenhaSegura123!")
    private String hashedPassword;
}

