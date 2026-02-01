package com.example.SpringChat.core.connection.entity;

import com.example.SpringChat.core.AbstractEntity;
import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.core.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class Connection extends AbstractEntity<UUID> {
    private User requester;
    private User receiver;
    private ConnectionStatus connectionStatus;


    public Connection(User requester, User receiver,ConnectionStatus connectionStatus){
        this.requester = requester;
        this.receiver = receiver;
        this.connectionStatus = connectionStatus;
    }
}
