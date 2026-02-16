package com.example.SpringChat.infrastructure.user.adapter.controller.mapper;


import com.example.SpringChat.application.user.responseDTO.UserDataResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDataResponseDTO toUserDataResponseDTO(User user);
}
