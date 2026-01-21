package com.example.SpringChat.application.user.usecases;

import com.example.SpringChat.application.user.command.CreateUserCommand;
import com.example.SpringChat.application.user.port.CreateUserInputPort;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.core.user.exception.UserEmailAlreadyExistsException;
import com.example.SpringChat.core.user.gateway.UserGateway;

import java.util.Random;

public class CreateUserUseCase implements CreateUserInputPort {
    private final UserGateway userGateway;
    private final Random random = new Random();

    public CreateUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public User execute(CreateUserCommand command) {
        if (userGateway.existsByEmail(command.email())) {
                throw new  UserEmailAlreadyExistsException("O e-mail " + command.email() + " Já está em uso.");
            }

        int publicIdentificationKey;
        do {
            publicIdentificationKey = random.nextInt(900000) + 100000;
        } while (userGateway.existsByPublicIdentificationKey(publicIdentificationKey));

        String hashedPassword = userGateway.encodePassword(command.hashedPassword());

        User newUser = new User(command.nickname(), command.email(), hashedPassword, publicIdentificationKey);

        return userGateway.save(newUser);
    }
}
