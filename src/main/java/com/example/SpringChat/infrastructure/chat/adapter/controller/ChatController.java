package com.example.SpringChat.infrastructure.chat.adapter.controller;

import com.example.SpringChat.application.chat.responseDTO.ChatMessageResponseDTO;
import com.example.SpringChat.application.chat.responseDTO.FindOrCreatePersonalChatResponseDTO;
import com.example.SpringChat.application.chat.responseDTO.SidebarItemResponseDTO;
import com.example.SpringChat.application.chat.service.ChatService;
import com.example.SpringChat.core.chat.entity.Chat;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("chats")
@Tag(name = "Chats", description = "Endpoints para gerenciamento de conversas diretas, sidebar e histórico de mensagens")
@SecurityRequirement(name = "bearerAuth")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/sidebar")
    @Operation(summary = "Listar conversas da barra lateral", description = "Retorna a lista de conversas recentes do usuário ordenadas pela data da última mensagem (Single Table GSI).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de conversas da sidebar obtida com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SidebarItemResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    public ResponseEntity<List<SidebarItemResponseDTO>> loadSidebar(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserEntity user
    ) {
        List<SidebarItemResponseDTO> sidebar = chatService.loadSidebar(user.getId());
        return ResponseEntity.ok(sidebar);
    }

    @GetMapping("/{targetUserId}")
    @Operation(summary = "Buscar ou criar chat privado", description = "Retorna o ID do chat direto existente entre o usuário autenticado e o usuário de destino (ou cria um novo atomicamente via DynamoDB).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat encontrado ou criado com sucesso",
                    content = @Content(schema = @Schema(implementation = FindOrCreatePersonalChatResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado / Token ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário de destino não encontrado", content = @Content)
    })
    public ResponseEntity<FindOrCreatePersonalChatResponseDTO> findOrCreateChat(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserEntity user,
            @Parameter(description = "UUID do usuário de destino", required = true)
            @PathVariable UUID targetUserId
    ) {
        Chat chat = chatService.findOrCreatePersonalChat(user.getId(), targetUserId);
        return ResponseEntity.ok(new FindOrCreatePersonalChatResponseDTO(chat.getId()));
    }

    @GetMapping("/{chatId}/messages")
    @Operation(summary = "Obter histórico de mensagens de um chat", description = "Retorna as mensagens de uma conversa com paginação reversa via ULID (ordem decrescente cronológica).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de mensagens obtido com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatMessageResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado: usuário não participa deste chat", content = @Content)
    })
    public ResponseEntity<List<ChatMessageResponseDTO>> getChatHistory(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserEntity user,
            @Parameter(description = "UUID do chat", required = true)
            @PathVariable UUID chatId,
            @Parameter(description = "ULID da mensagem mais antiga já carregada para cursor pagination (opcional)")
            @RequestParam(required = false) String before,
            @Parameter(description = "Quantidade máxima de mensagens a retornar (padrão: 50)")
            @RequestParam(defaultValue = "50") int limit
    ) {
        chatService.validateChatAccess(user.getId().toString(), chatId.toString());
        List<ChatMessageResponseDTO> messages = chatService.findChatHistory(chatId, before, limit);
        return ResponseEntity.ok(messages);
    }
}
