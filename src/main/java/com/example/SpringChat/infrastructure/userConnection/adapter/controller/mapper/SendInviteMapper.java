package com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper;

import com.example.SpringChat.application.connection.responseDTO.SendInviteResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SendInviteMapper {

    @Mapping(source = "id", target="connectionId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    SendInviteResponseDTO toResponse(Connection connection);
}
