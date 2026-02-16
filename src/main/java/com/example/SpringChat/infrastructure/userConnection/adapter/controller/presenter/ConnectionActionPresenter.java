package com.example.SpringChat.infrastructure.userConnection.adapter.controller.presenter;

import com.example.SpringChat.application.connection.responseDTO.AcceptInviteResponseDTO;
import com.example.SpringChat.application.connection.responseDTO.SendInviteResponseDTO;
import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper.AcceptInviteMapper;
import com.example.SpringChat.infrastructure.userConnection.adapter.controller.mapper.SendInviteMapper;
import org.springframework.stereotype.Component;

@Component
public class ConnectionActionPresenter {
    private final AcceptInviteMapper acceptInviteMapper;
    private final SendInviteMapper sendInviteMapper;

    public ConnectionActionPresenter(AcceptInviteMapper acceptInviteMapper, SendInviteMapper sendInviteMapper){
        this.acceptInviteMapper = acceptInviteMapper;
        this.sendInviteMapper = sendInviteMapper;
    }

    public AcceptInviteResponseDTO toAcceptInvitesResponse(Connection domainResponse){
        return acceptInviteMapper.toResponse(domainResponse);
    }

    public SendInviteResponseDTO toSendInvitesResponse(Connection domainResponse){
        return sendInviteMapper.toResponse(domainResponse);
    }
}
