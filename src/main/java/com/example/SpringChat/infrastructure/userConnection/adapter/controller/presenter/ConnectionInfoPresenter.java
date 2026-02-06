package com.example.SpringChat.infrastructure.userConnection.adapter.controller.presenter;

import com.example.SpringChat.application.connection.responseDTO.InvitesReceivedResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.InvitesSentResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.UserFriendsResponseDTO;
import com.example.SpringChat.application.shared.response.PaginationResponse;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper.InvitesReceivedMapper;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper.InvitesSentMapper;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper.UserFriendsMapper;
import org.springframework.stereotype.Component;

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

    public PaginationResponse<InvitesSentResponseDTO> toInvitesSentResponse(PaginationResponse<Connection> domainResponse){
        return invitesSentMapper.toPaginationResponse(domainResponse);
    }

    public PaginationResponse<InvitesReceivedResponseDTO> toInviteReceivedResponse(PaginationResponse<Connection> domainResponse){
        return invitesReceivedMapper.toPaginationResponse(domainResponse);
    }

    public PaginationResponse<UserFriendsResponseDTO> toUserFriendsResponse(PaginationResponse<Connection> domainResponse){
        return userFriendsMapper.toPaginationResponse(domainResponse);
    }
}
