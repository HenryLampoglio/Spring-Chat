package com.example.SpringChat.application.connection.usecases;

import com.example.SpringChat.application.connection.command.CancelConnectionCommand;
import com.example.SpringChat.application.connection.usecase.CancelConnectionUseCase;
import com.example.SpringChat.core.connection.exception.ConnectionsNotFoundException;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.user.exception.UserNotFoundException;
import com.example.SpringChat.core.user.gateway.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.eq;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CancelConnectionUseCaseTest {
    @Mock
    ConnectionGateway connectionGateway;

    @InjectMocks
    CancelConnectionUseCase useCase;

    @Test
    void shouldCancelConnectionSuccessfully()
    {
        UUID connectionId = UUID.randomUUID();
        CancelConnectionCommand command = new CancelConnectionCommand(connectionId);

        //arrange
        Mockito.when(connectionGateway.connectionExists(eq(connectionId))).thenReturn(true);

        //act
        useCase.execute(command);

        //assert
        InOrder inOrder = Mockito.inOrder(connectionGateway);
        inOrder.verify(connectionGateway).connectionExists(eq(connectionId));
        inOrder.verify(connectionGateway).cancelConnection(eq(connectionId));

        Mockito.verify(connectionGateway, Mockito.times(1)).connectionExists(eq(connectionId));
        Mockito.verify(connectionGateway, Mockito.times(1)).cancelConnection(eq(connectionId));

        Mockito.verifyNoMoreInteractions(connectionGateway);
    }

    @Test
    void shouldThrowExceptionConnectionsNotFoundException(){
        UUID connectionId = UUID.randomUUID();
        CancelConnectionCommand command = new CancelConnectionCommand(connectionId);

        //arrange
        Mockito.when(connectionGateway.connectionExists(eq(connectionId))).thenReturn(false);

        //act
        String message = Assertions.assertThrows(ConnectionsNotFoundException.class, () ->{
            useCase.execute(command);
        }).getMessage();

        //assert
        Assertions.assertEquals("this connections doesn't exists", message);
        Mockito.verify(connectionGateway, Mockito.times(1)).connectionExists(eq(connectionId));
        Mockito.verify(connectionGateway, Mockito.times(0)).cancelConnection(eq(connectionId));
    }
}
