package com.example.SpringChat.application.user.port;

import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication.LoginResponse;

public interface LoginInputPort {
    LoginResponse execute(LoginCommand command);
}
