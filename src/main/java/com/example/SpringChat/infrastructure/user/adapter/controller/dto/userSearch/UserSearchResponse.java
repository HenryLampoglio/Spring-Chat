package com.example.SpringChat.infrastructure.user.adapter.controller.dto.userSearch;

import com.example.SpringChat.core.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
public class UserSearchResponse {
    private List<User> usersList;
}
