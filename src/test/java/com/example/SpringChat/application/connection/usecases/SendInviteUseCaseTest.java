package com.example.SpringChat.application.connection.usecases;

import com.example.SpringChat.application.connection.command.SendInviteCommand;
import com.example.SpringChat.application.connection.usecase.SendInviteUseCase;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.core.user.exception.UserNotFoundException;
import com.example.SpringChat.core.user.gateway.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.eq;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class SendInviteUseCaseTest {

    @Mock
    ConnectionGateway connectionGateway;

    @Mock
    UserGateway userGateway;

   @InjectMocks
    private SendInviteUseCase useCase;

   @Test
    void shouldCreateConnectionSuccessfully() {
        UUID requesterId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();

       //arrange
       Mockito.when(userGateway.userExists(eq(receiverId))).thenReturn(true);

       Mockito.when(connectionGateway.sendInvite(eq(requesterId), eq(receiverId), eq(ConnectionStatus.pending)))
               .thenReturn(new Connection());

       //act
       SendInviteCommand command = new SendInviteCommand(requesterId, receiverId);

       Connection response = useCase.execute(command);

       //assert
       Assertions.assertNotNull(response);

       Mockito.verify(userGateway, Mockito.times(1)).userExists(eq(receiverId));
       Mockito.verify(connectionGateway, Mockito.times(1)).sendInvite(eq(requesterId), eq(receiverId), eq(ConnectionStatus.pending));
   }

   @Test
    void shouldThrowExceptionUserNOtFound(){
       UUID requesterId = UUID.randomUUID();
       UUID receiverId = UUID.randomUUID();

       //Arrange
       Mockito.when(userGateway.userExists(receiverId)).thenReturn(false);

       //act
       SendInviteCommand command = new SendInviteCommand(requesterId, receiverId);

       String message = Assertions.assertThrows(UserNotFoundException.class, () ->{
           useCase.execute(command);
       }).getMessage();

       //assert
       Assertions.assertEquals("Usuário não encontrado", message);
       Mockito.verify(userGateway, Mockito.times(1)).userExists(eq(receiverId));
       Mockito.verify(connectionGateway, Mockito.times(0)).sendInvite(eq(requesterId), eq(receiverId), eq(ConnectionStatus.pending));
   }
}
