package com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper;

import com.example.SpringChat.application.connection.responseDTO.UserFriendsResponseDTO;
import com.example.SpringChat.application.user.responseDTO.UserDataResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.user.adapter.controller.mapper.UserMapper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class UserFriendsMapper {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(source = "id", target = "connectionId")
    @Mapping(target = "userData", expression = "java(mapCorrectFriend(connection, loggedUserId))")
    public abstract UserFriendsResponseDTO toResponse(Connection connection, @Context UUID loggedUserId);

    protected UserDataResponseDTO mapCorrectFriend(Connection connection, UUID loggedUserId) {
        User friend = connection.getRequester().getId().equals(loggedUserId)
                ? connection.getReceiver()
                : connection.getRequester();

        return userMapper.toUserDataResponseDTO(friend);
    }
}