package com.example.SpringChat.infrastructure.user.adapter.controller;

import com.example.SpringChat.application.user.command.SearchUsersCommand;
import com.example.SpringChat.application.user.port.SearchUsersInputPort;
import com.example.SpringChat.application.user.responseDTO.SearchUserResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.userSearch.UserSearchResponse;
import com.example.SpringChat.infrastructure.user.adapter.controller.presenter.UserPresenter;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    private final SearchUsersInputPort searchUsersInputPort;
    private final UserPresenter userPresenter;

    public UserController(SearchUsersInputPort searchUsersInputPort, UserPresenter userPresenter){
        this.searchUsersInputPort = searchUsersInputPort;
        this.userPresenter = userPresenter;
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchUserResponseDTO>> searchUser(
            @RequestParam(value = "userIdentifier", required = true)
            String userIdentifier,
            @AuthenticationPrincipal UserEntity user
    )
    {
        SearchUsersCommand command = new SearchUsersCommand(userIdentifier, user.getId());
        List<User> foundUsers = searchUsersInputPort.execute(command);
        List<SearchUserResponseDTO> response = userPresenter.toSearchUsersListResponse(foundUsers);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
