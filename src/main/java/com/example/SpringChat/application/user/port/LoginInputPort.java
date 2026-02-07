package com.example.SpringChat.application.user.port;

import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.application.user.responseDTO.LoginResponseDTO;

public interface LoginInputPort {
    LoginResponseDTO execute(LoginCommand command);
}
