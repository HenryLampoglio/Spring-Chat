package com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper.InvitesSent;

import com.example.SpringChat.application.connection.responseDTO.InvitesSentResponseDTO;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvitesSentMapper {

    PaginationResponse<InvitesSentResponseDTO> toPaginationResponse(PaginationResponse<Connection> pagination);

    @Mapping(source = "receiver.nickname", target = "receiverNickname")
    @Mapping(source = "receiver.publicIdentificationKey", target = "receiverPublicIdentificationKey")
    @Mapping(source = "id", target = "connectionId")
    @Mapping(source = "connectionStatus", target = "connectionStatus")
    InvitesSentResponseDTO toResponse(Connection connection);
}