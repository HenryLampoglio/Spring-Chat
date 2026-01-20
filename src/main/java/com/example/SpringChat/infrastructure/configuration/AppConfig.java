package com.example.SpringChat.infrastructure.configuration;

import com.example.SpringChat.application.connection.port.*;
import com.example.SpringChat.application.connection.usecase.*;
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

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserGateway userGateway(SpringUserRepository springUserRepository, BCryptPasswordEncoder passwordEncoder, TokenService tokenService) {
        return new UserGatewayAdapter(springUserRepository, passwordEncoder, tokenService) {};
    }

    @Bean
    public ConnectionGateway connectionGateway(SpringConnectionRepository springConnectionRepository, SpringUserRepository springUserRepository){
        return new ConnectionGatewayAdapter(springConnectionRepository, springUserRepository){};
    }

    @Bean
    public CreateUserInputPort createUserInputPort(UserGateway userGateway) {
        return new CreateUserUseCase(userGateway);
    }

    @Bean
    public LoginInputPort loginInputPort(UserGateway userGateway){
        return new LoginUseCase(userGateway);
    }

    @Bean
    public SearchUsersInputPort searchUsersInputPort(UserGateway userGateway){
        return new SearchUsersUseCase(userGateway);
    }

    @Bean
    public UserFriendsInputPort userFriendsInputPort(ConnectionGateway connectionGateway){
        return new RetrieveUserFriendsUseCase(connectionGateway);
    }

    @Bean
    public SendInvitePort sendInvitePort(ConnectionGateway connectionGateway, UserGateway userGateway){
        return new SendInviteUseCase(connectionGateway,userGateway);
    }

    @Bean
    public CancelConnectionPort cancelInvitePort(ConnectionGateway connectionGateway){
        return new CancelConnectionUseCase(connectionGateway);
    }

    @Bean
    public AcceptInvitePort acceptInvitePortPort(ConnectionGateway connectionGateway){
        return new AcceptInviteUseCase(connectionGateway);
    }

    @Bean
    public InvitesSentInputPort invitesSentInputPort(ConnectionGateway connectionGateway){
        return new GetInvitesSentUseCase(connectionGateway);
    }

    @Bean InvitesReceivedInputPort invitesReceivedInputPort(ConnectionGateway connectionGateway){
        return new GetInvitesReceivedUseCase(connectionGateway);
    }
}