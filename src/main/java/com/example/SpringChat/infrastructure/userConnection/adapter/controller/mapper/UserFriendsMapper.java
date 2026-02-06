package com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper;

import com.example.SpringChat.application.connection.responseDTO.UserFriendsResponseDTO;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserFriendsMapper {

    PaginationResponse<UserFriendsResponseDTO> toPaginationResponse(PaginationResponse<Connection> pagination);

    @Mapping(source = "receiver.nickname", target = "friendNickname")
    @Mapping(source = "receiver.publicIdentificationKey", target = "friendPublicIdentificationKey")
    @Mapping(source = "id", target = "connectionId")
    @Mapping(source = "connectionStatus", target = "connectionStatus")
    UserFriendsResponseDTO toResponse(Connection connection);
}
