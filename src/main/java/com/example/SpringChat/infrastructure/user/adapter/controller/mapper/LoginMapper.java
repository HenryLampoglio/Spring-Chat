package com.example.SpringChat.infrastructure.user.adapter.controller.mapper;

import com.example.SpringChat.application.connection.responseDTO.InvitesSentResponseDTO;
import com.example.SpringChat.application.user.output.LoginOutput;
import com.example.SpringChat.application.user.responseDTO.LoginResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LoginMapper {

    LoginResponseDTO toResponse(LoginOutput loginOutput);
}
