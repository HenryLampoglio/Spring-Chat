package com.example.SpringChat.application.user.usecase;

// 1. IMPORT the implementation class explicitly
import com.example.SpringChat.application.connection.usecase.RetrieveUserFriendsUseCase;
import com.example.SpringChat.application.connection.command.UserFriendsCommand;
import com.example.SpringChat.application.shared.request.PaginationRequest;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.connection.gateway.ConnectionGateway;
import com.example.SpringChat.core.user.gateway.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class RetrieveUserFriendsUseCaseTest { // 2. RENAME this class (Added 'Test' suffix)

    @Mock
    private ConnectionGateway connectionGateway;

    @Mock
    private PaginationRequest paginationRequest;

    @InjectMocks
    private RetrieveUserFriendsUseCase useCase; // Now this refers to the imported class

    private UserFriendsCommand command;

    @BeforeEach
    void setUp(){
        command = new UserFriendsCommand(UUID.fromString("00000000-0000-0000-0000-000000000001"), paginationRequest);
    }

    @Test
    void shouldReturnFriendsSuccessfully() {
        Mockito.when(connectionGateway.searchUsers(any(), eq(paginationRequest)))
                .thenReturn(new PaginationResponse<>(
                        List.of(new Connection()),
                        1L,
                        1
                ));

       PaginationResponse<Connection> response = useCase.execute(command);

        Assertions.assertNotNull(response.getItems());
        Mockito.verify(connectionGateway, Mockito.times(1)).searchUsers(any(), eq(paginationRequest));
    }

}