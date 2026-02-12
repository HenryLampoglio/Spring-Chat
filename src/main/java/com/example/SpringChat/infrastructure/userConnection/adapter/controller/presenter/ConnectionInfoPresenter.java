package com.example.SpringChat.infrastructure.userConnection.adapter.controller.presenter;

import com.example.SpringChat.application.connection.responseDTO.InvitesReceivedResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.InvitesSentResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.UserFriendsResponseDTO;
import com.example.SpringChat.application.shared.response.PaginationResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper.InvitesReceivedMapper;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper.InvitesSentMapper;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper.UserFriendsMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConnectionInfoPresenter {
    private final InvitesSentMapper invitesSentMapper;
    private final InvitesReceivedMapper invitesReceivedMapper;
    private final UserFriendsMapper userFriendsMapper;

    public ConnectionInfoPresenter(InvitesSentMapper invitesSentMapper, InvitesReceivedMapper invitesReceivedMapper, UserFriendsMapper userFriendsMapper){
        this.invitesSentMapper = invitesSentMapper;
        this.invitesReceivedMapper = invitesReceivedMapper;
        this.userFriendsMapper = userFriendsMapper;
    }

    public InvitesSentResponseDTO toInvitesSentResponse(Connection domainResponse){
        return invitesSentMapper.toResponse(domainResponse);
    }

    public InvitesReceivedResponseDTO toInviteReceivedResponse(Connection domainResponse){
        return invitesReceivedMapper.toResponse(domainResponse);
    }

    public UserFriendsResponseDTO toUserFriendsResponse(Connection domainResponse, UUID userId){
        return userFriendsMapper.toResponse(domainResponse, userId);
    }
}
