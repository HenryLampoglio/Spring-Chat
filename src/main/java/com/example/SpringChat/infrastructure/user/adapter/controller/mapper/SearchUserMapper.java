package com.example.SpringChat.infrastructure.user.adapter.controller.mapper;

import com.example.SpringChat.application.user.responseDTO.SearchUserResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchUserMapper {

    List<SearchUserResponseDTO> toSearchUsersListResponse(List<User> userList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nickname", target = "nickname")
    @Mapping(source = "publicIdentificationKey", target = "publicIdentificationKey")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    SearchUserResponseDTO toResponse(User user);
}
