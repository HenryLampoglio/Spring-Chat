package com.example.SpringChat.application.user.port;

import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.core.user.entity.User;

public interface LoginInputPort {
    String execute(LoginCommand command);
}
