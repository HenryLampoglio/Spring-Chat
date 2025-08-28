package com.example.SpringChat.application.user.port;

import com.example.SpringChat.application.user.command.CreateUserCommand;
import com.example.SpringChat.core.user.entity.User;

public interface CreateUserInputPort {
    User execute(CreateUserCommand command);
}
