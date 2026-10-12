package com.example.SpringChat.infrastructure.user.adapter.controller;

import com.example.SpringChat.application.user.responseDTO.SearchUserResponseDTO;
import com.example.SpringChat.application.user.service.UserService;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@Tag(name = "Usuários", description = "Endpoints para consulta e pesquisa de usuários")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar usuários", description = "Pesquisa usuários cadastrados no sistema pelo seu nome/nickname ou identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários encontrada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SearchUserResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Não autorizado / Token ausente ou inválido", content = @Content)
    })
    public ResponseEntity<List<SearchUserResponseDTO>> searchUser(
            @Parameter(description = "Identificador ou termo de busca do usuário (ex: nickname)", required = true)
            @RequestParam(value = "userIdentifier", required = true)
            String userIdentifier,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserEntity user
    ) {
        List<SearchUserResponseDTO> response = userService.searchUsers(userIdentifier, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
