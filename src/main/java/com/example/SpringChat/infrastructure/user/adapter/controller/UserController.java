package com.example.SpringChat.infrastructure.user.adapter.controller;

import com.example.SpringChat.application.user.command.SearchUsersCommand;
import com.example.SpringChat.application.user.port.SearchUsersInputPort;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.user.adapter.controller.dto.userSearch.UserSearchResponse;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    private final SearchUsersInputPort searchUsersInputPort;

    public UserController(SearchUsersInputPort searchUsersInputPort){
        this.searchUsersInputPort = searchUsersInputPort;
    }

    @GetMapping("/search")
    public ResponseEntity<UserSearchResponse> searchUser(
            @RequestParam(value = "userIdentifier", required = true)
            String userIdentifier,
            @AuthenticationPrincipal UserEntity user
    )
    {
        System.out.println(user.getId());
        SearchUsersCommand command = new SearchUsersCommand(userIdentifier, user.getId());
        List<User> foundUsers = searchUsersInputPort.execute(command);
        UserSearchResponse response = new UserSearchResponse(
            foundUsers
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
