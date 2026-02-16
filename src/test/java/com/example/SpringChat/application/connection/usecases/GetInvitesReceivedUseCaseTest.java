package com.example.SpringChat.application.connection.usecases;

import com.example.SpringChat.application.connection.command.InvitesReceivedCommand;
import com.example.SpringChat.application.connection.usecase.GetInvitesReceivedUseCase;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.application.shared.response.PaginationResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.exception.InvitesSolicitationsNotFoundException;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class GetInvitesReceivedUseCaseTest {

    @Mock
    ConnectionGateway connectionGateway;

    @Mock
    PaginationRequest paginationRequest;

    @InjectMocks
    private GetInvitesReceivedUseCase useCase;

    @Test
    void shouldRetrieveInvitesReceivedSuccessfully(){
        UUID userId = UUID.randomUUID();


        //arrange

        Mockito.when(connectionGateway.getInvitesReceivedByUser(eq(userId),eq(paginationRequest),eq(ConnectionStatus.pending)))
                .thenReturn(new PaginationResponseDTO<>(
                        List.of(new Connection()),
                        1,
                        1
                ));
        //act
        InvitesReceivedCommand command = new InvitesReceivedCommand(userId,paginationRequest);

        PaginationResponseDTO<Connection> response = useCase.execute(command);

        //assert
        Assertions.assertNotNull(response);
        Mockito.verify(connectionGateway,Mockito.times(1)).getInvitesReceivedByUser(eq(userId),eq(paginationRequest),eq(ConnectionStatus.pending));
    }

    @Test
    void shouldThrowExceptionInvitesSolicitationsNotFoundException(){
        UUID userId = UUID.randomUUID();

        //arrange
        Mockito.when(connectionGateway.getInvitesReceivedByUser(eq(userId),eq(paginationRequest),eq(ConnectionStatus.pending)))
                .thenReturn(new PaginationResponseDTO<>(
                        Collections.emptyList(),
                        1,
                        1
                ));

        //act
        InvitesReceivedCommand command = new InvitesReceivedCommand(userId,paginationRequest);

        String message = Assertions.assertThrows(InvitesSolicitationsNotFoundException.class, () ->{
            useCase.execute(command);
        }).getMessage();

        //assert
        Assertions.assertEquals("Não existe nenhum convite pendente para você", message);
        Mockito.verify(connectionGateway,Mockito.times(1)).getInvitesReceivedByUser(eq(userId),eq(paginationRequest),eq(ConnectionStatus.pending));
    }
}
