package com.example.SpringChat.application.user.port;

import com.example.SpringChat.application.user.command.SearchUsersCommand;
import com.example.SpringChat.core.user.entity.User;

import java.util.List;

public interface SearchUsersInputPort {
    List<User> execute(SearchUsersCommand command);
}
