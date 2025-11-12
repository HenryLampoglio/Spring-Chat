package com.example.SpringChat.application.user.usecase;

import com.example.SpringChat.application.user.command.SearchUsersCommand;
import com.example.SpringChat.application.user.port.SearchUsersInputPort;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.gateway.UserGateway;

import java.util.List;

public class SearchUsersUseCase implements SearchUsersInputPort {
    private final UserGateway userGateway;

    public SearchUsersUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public List<User> execute(SearchUsersCommand command) {
        String nickname = command.getNicknamePart();
        int publicIdentificationKey = command.getKeyPart();

        return userGateway.findTop10NicknameContainingAndPublicIdentificationKeyContaining(nickname, publicIdentificationKey);
    }
}
