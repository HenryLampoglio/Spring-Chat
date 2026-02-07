package com.example.SpringChat.infrastructure.user.adapter.controller.mapper;

import com.example.SpringChat.application.user.responseDTO.SearchUserResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchUserMapper {

    List<SearchUserResponseDTO> toSearchUsersListResponse(List<User> userList);

}
