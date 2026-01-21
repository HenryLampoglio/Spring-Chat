package com.example.SpringChat.application.connection.usecases;

import com.example.SpringChat.application.connection.command.SendInviteCommand;
import com.example.SpringChat.application.connection.usecase.SendInviteUseCase;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.enums.ConnectionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class SendInviteUseCaseTest {

    @Mock
    ConnectionGateway connectionGateway;

   @InjectMocks
    private SendInviteUseCase useCase;

   private SendInviteCommand command;

   @BeforeEach
    void setUp(){
       command = new SendInviteCommand(UUID.fromString("00000000-0000-0000-0000-000000000001"),UUID.fromString("00000000-0000-0000-0000-000000000001"));
   }

   @Test
    void shouldCreateConnectionSuccessfully() {
       Mockito.when(connectionGateway.sendInvite(any(), any(), any()))
               .thenReturn(new Connection());
   }
}
