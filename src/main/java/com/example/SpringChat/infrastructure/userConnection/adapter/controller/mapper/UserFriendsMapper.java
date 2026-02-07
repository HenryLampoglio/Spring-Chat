package com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper;

import com.example.SpringChat.application.connection.responseDTO.UserFriendsResponseDTO;
import com.example.SpringChat.application.shared.response.PaginationResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.infrastructure.user.adapter.controller.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserFriendsMapper {

    @Mapping(source = "id", target = "connectionId")
    @Mapping(source = "receiver", target = "receiverData")
    UserFriendsResponseDTO toResponse(Connection connection);
}
