package com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper;

import com.example.SpringChat.application.connection.responseDTO.SendInviteResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SendInviteMapper {

    @Mapping(source = "id", target="connectionId")
    SendInviteResponseDTO toResponse(Connection connection);
}
