package com.example.SpringChat.application.user.port;

import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.application.user.output.LoginOutput;

public interface LoginInputPort {
    LoginOutput execute(LoginCommand command);
}
