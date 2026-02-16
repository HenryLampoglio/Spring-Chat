package com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper;

import com.example.SpringChat.application.connection.responseDTO.AcceptInviteResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AcceptInviteMapper {

    @Mapping(source = "id", target="connectionId")
    AcceptInviteResponseDTO toResponse(Connection connection);
}
