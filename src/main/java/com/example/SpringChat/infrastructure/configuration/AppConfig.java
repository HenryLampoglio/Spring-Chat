package com.example.SpringChat.infrastructure.configuration;

import com.example.SpringChat.application.connection.port.UserFriendsInputPort;
import com.example.SpringChat.application.connection.usecase.RetrieveUserFriendsUseCase;
import com.example.SpringChat.application.user.port.CreateUserInputPort;
import com.example.SpringChat.application.user.port.LoginInputPort;
import com.example.SpringChat.application.user.port.SearchUsersInputPort;
import com.example.SpringChat.application.user.usecase.CreateUserUseCase;
import com.example.SpringChat.application.user.usecase.LoginUseCase;
import com.example.SpringChat.application.user.usecase.SearchUsersUseCase;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.user.gateway.UserGateway;
import com.example.SpringChat.infrastructure.security.TokenService;
import com.example.SpringChat.infrastructure.user.adapter.persistence.UserGatewayAdapter;
import com.example.SpringChat.infrastructure.user.persistence.repository.SpringUserRepository;
import com.example.SpringChat.infrastructure.userConnection.adapter.persistence.ConnectionGatewayAdapter;
import com.example.SpringChat.infrastructure.userConnection.persistence.repository.SpringConnectionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    // 1. Cria a instância do BCryptPasswordEncoder
    // O BCryptPasswordEncoder é uma dependência externa que é usada pelo UseCase
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Cria a instância do UserGateway (o adaptador)
    // Spring injeta o SpringUserRepository aqui.
    @Bean
    public UserGateway userGateway(SpringUserRepository springUserRepository) {
        return new UserGatewayAdapter(springUserRepository) {};
    }

    @Bean
    public ConnectionGateway connectionGateway(SpringConnectionRepository springConnectionRepository){
        return new ConnectionGatewayAdapter(springConnectionRepository){};
    }

    // 3. Cria a instância do UseCase
    // Spring injeta o UserGateway e o BCryptPasswordEncoder aqui.
    @Bean
    public CreateUserInputPort createUserInputPort(UserGateway userGateway, BCryptPasswordEncoder passwordEncoder) {
        return new CreateUserUseCase(userGateway, passwordEncoder);
    }

    @Bean
    public LoginInputPort loginInputPort(UserGateway userGateway, BCryptPasswordEncoder passwordEncoder, TokenService tokenService){
        return new LoginUseCase(userGateway, passwordEncoder, tokenService);
    }

    @Bean
    public SearchUsersInputPort searchUsersInputPort(UserGateway userGateway){
        return new SearchUsersUseCase(userGateway);
    }

    @Bean
    public UserFriendsInputPort userFriendsInputPort(ConnectionGateway connectionGateway){
        return new RetrieveUserFriendsUseCase(connectionGateway);
    }
}