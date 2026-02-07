package com.example.SpringChat.infrastructure.user.adapter.controller.mapper;

import com.example.SpringChat.application.user.responseDTO.CreateUserResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateUserMapper {

    @Mapping(source = "id", target="id")
    @Mapping(source = "nickname", target = "nickname")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "publicIdentificationKey", target = "publicIdentificationKey")
    @Mapping(source = "createdAt", target = "createdAt")
    CreateUserResponseDTO toResponse(User user);
}
