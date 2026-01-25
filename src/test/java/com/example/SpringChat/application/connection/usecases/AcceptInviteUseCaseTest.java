package com.example.SpringChat.application.connection.usecases;


import com.example.SpringChat.application.connection.command.AcceptInviteCommand;
import com.example.SpringChat.application.connection.usecase.AcceptInviteUseCase;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.exception.ConnectionsNotFoundException;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.enums.ConnectionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AcceptInviteUseCaseTest {

    @Mock
    ConnectionGateway connectionGateway;

    @InjectMocks
    private AcceptInviteUseCase useCase;

    @Test
    void shouldAcceptInviteSuccessfully(){
        UUID connectionId = UUID.randomUUID();

        //arrange
        Mockito.when(connectionGateway.acceptInvite(eq(connectionId), eq(ConnectionStatus.pending)))
                .thenReturn(Optional.of(new Connection()));

        //act
        AcceptInviteCommand command = new AcceptInviteCommand(connectionId);
        Connection response = useCase.execute(command);

        //assert
        Assertions.assertNotNull(response);
        Mockito.verify(connectionGateway, Mockito.times(1)).acceptInvite(eq(connectionId), eq(ConnectionStatus.pending));
    }

    @Test
    void shouldThrowExceptionConnectionsNotFoundException(){
        UUID connectionId = UUID.randomUUID();

        //arrange
        Mockito.when(connectionGateway.acceptInvite(eq(connectionId), eq(ConnectionStatus.pending)))
                .thenReturn(Optional.empty());

        //act
        AcceptInviteCommand command = new AcceptInviteCommand(connectionId);

        String message = Assertions.assertThrows(ConnectionsNotFoundException.class, () ->{
            useCase.execute(command);
        }).getMessage();

        //
        Assertions.assertEquals("this request doesn't exist or doesn't have the pending status", message);
        Mockito.verify(connectionGateway, Mockito.times(1)).acceptInvite(eq(connectionId), eq(ConnectionStatus.pending));
    }
}
