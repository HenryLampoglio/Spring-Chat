package com.example.SpringChat.infrastructure.user.adapter.controller.presenter;

import com.example.SpringChat.application.user.responseDTO.CreateUserResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.user.adapter.controller.mapper.CreateUserMapper;
import com.example.SpringChat.infrastructure.user.adapter.controller.mapper.SearchUserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserPresenter {
    private final CreateUserMapper createUserMapper;
    private final SearchUserMapper searchUserMapper;

    public UserPresenter(CreateUserMapper createUserMapper, SearchUserMapper searchUserMapper)
    {
        this.createUserMapper = createUserMapper;
        this.searchUserMapper = searchUserMapper;
    }

    public CreateUserResponseDTO toCreateUserResponse(User domainResponse){
        return createUserMapper.toResponse(domainResponse);
    }
}
