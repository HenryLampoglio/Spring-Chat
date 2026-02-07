package com.example.SpringChat.application.user.usecases;

import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.application.user.port.LoginInputPort;
import com.example.SpringChat.application.user.responseDTO.LoginResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.PasswordsDoesntMatchesException;
import com.example.SpringChat.core.user.exception.UserEmailNotFoundException;
import com.example.SpringChat.core.user.gateway.UserGateway;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.authentication.LoginResponse;

public class LoginUseCase implements LoginInputPort {
    private final UserGateway userGateway;

    public LoginUseCase(UserGateway userGateway){
        this.userGateway = userGateway;
    }

    @Override
    public LoginResponseDTO execute(LoginCommand command) {
        User user = userGateway.findByEmail(command.Email()).
                orElseThrow(() ->new UserEmailNotFoundException("Usuário com esse email não encontrado"));
        boolean passwordMatches = userGateway.validatePassword(command.password(), user.getHashedPassword());

        if(!passwordMatches) throw new PasswordsDoesntMatchesException("Senha Incorreta.");


        String userToken = userGateway.generateAuthToken(user);
        return new LoginResponseDTO(userToken, user.getEmail(), user.getNickname(), user.getPublicIdentificationKey());
    }
}
