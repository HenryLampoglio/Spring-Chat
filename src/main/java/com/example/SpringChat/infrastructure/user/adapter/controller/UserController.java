package com.example.SpringChat.infrastructure.user.adapter.controller;

import com.example.SpringChat.application.user.command.SearchUsersCommand;
import com.example.SpringChat.application.user.port.SearchUsersInputPort;
import com.example.SpringChat.application.user.responseDTO.SearchUserResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.user.adapter.controller.presenter.UserPresenter;
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
    private final SearchUsersInputPort searchUsersInputPort;
    private final UserPresenter userPresenter;

    public UserController(SearchUsersInputPort searchUsersInputPort, UserPresenter userPresenter){
        this.searchUsersInputPort = searchUsersInputPort;
        this.userPresenter = userPresenter;
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
    )
    {
        SearchUsersCommand command = new SearchUsersCommand(userIdentifier, user.getId());
        List<User> foundUsers = searchUsersInputPort.execute(command);
        List<SearchUserResponseDTO> response = userPresenter.toSearchUsersListResponse(foundUsers);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
