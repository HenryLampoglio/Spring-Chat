package com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper;

import com.example.SpringChat.application.connection.responseDTO.InvitesReceivedResponseDTO;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvitesReceivedMapper {
    PaginationResponse<InvitesReceivedResponseDTO> toPaginationResponse(PaginationResponse<Connection> pagination);

    @Mapping(source = "requester.nickname", target = "requesterNickname")
    @Mapping(source = "requester.publicIdentificationKey", target = "requesterPublicIdentificationKey")
    @Mapping(source = "id", target = "connectionId")
    @Mapping(source = "connectionStatus", target = "connectionStatus")
    InvitesReceivedResponseDTO toResponse(Connection connection);
}
