package com.example.SpringChat.application.user.usecase;

import com.example.SpringChat.application.user.command.LoginCommand;
import com.example.SpringChat.application.user.port.LoginInputPort;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.PasswordsDoesntMatchesException;
import com.example.SpringChat.core.user.exception.UserEmailNotFoundException;
import com.example.SpringChat.core.user.gateway.UserGateway;
import com.example.SpringChat.infrastructure.security.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LoginUseCase implements LoginInputPort {
    private final UserGateway userGateway;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public LoginUseCase(UserGateway userGateway, BCryptPasswordEncoder passwordEncoder, TokenService tokenService){
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public String execute(LoginCommand command) {
        User user = userGateway.findByEmail(command.Email()).
                orElseThrow(() ->new UserEmailNotFoundException("Usuário não encontrado"));

        boolean passwordMatches = passwordEncoder.matches(command.password(), user.getHashedPassword());

        if(!passwordMatches) throw new PasswordsDoesntMatchesException("Senha Incorreta.");

        return tokenService.generateToken(user);
    }
}
