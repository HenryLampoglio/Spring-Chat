package com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Requisição para autenticação de usuário (Login)")
public class LoginRequest {
    @Schema(description = "Endereço de e-mail cadastrado", example = "joao@email.com")
    private String email;

    @Schema(description = "Senha do usuário", example = "SenhaSegura123!")
    private String Password;
}

