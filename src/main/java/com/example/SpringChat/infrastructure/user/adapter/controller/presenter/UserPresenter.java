package com.example.SpringChat.infrastructure.user.adapter.controller.presenter;

import com.example.SpringChat.application.user.output.LoginOutput;
import com.example.SpringChat.application.user.responseDTO.CreateUserResponseDTO;
import com.example.SpringChat.application.user.responseDTO.LoginResponseDTO;
import com.example.SpringChat.application.user.responseDTO.SearchUserResponseDTO;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.user.adapter.controller.mapper.CreateUserMapper;
import com.example.SpringChat.infrastructure.user.adapter.controller.mapper.LoginMapper;
import com.example.SpringChat.infrastructure.user.adapter.controller.mapper.SearchUserMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserPresenter {
    private final CreateUserMapper createUserMapper;
    private final SearchUserMapper searchUserMapper;
    private final LoginMapper loginMapper;

    public UserPresenter(CreateUserMapper createUserMapper, SearchUserMapper searchUserMapper, LoginMapper loginMapper)
    {
        this.createUserMapper = createUserMapper;
        this.searchUserMapper = searchUserMapper;
        this.loginMapper = loginMapper;
    }

    public CreateUserResponseDTO toCreateUserResponse(User domainResponse){
        return createUserMapper.toResponse(domainResponse);
    }

    public List<SearchUserResponseDTO> toSearchUsersListResponse(List<User> domainResponse)
    {
        return searchUserMapper.toSearchUsersListResponse(domainResponse);
    }

    public LoginResponseDTO toLoginResponse(LoginOutput domainResponse)
    {
        return loginMapper.toResponse(domainResponse);
    }
}
