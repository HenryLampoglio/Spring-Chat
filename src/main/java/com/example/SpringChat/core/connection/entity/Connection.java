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
    private User user;
    private User friend;
    private ConnectionStatus connectionStatus;


    public Connection(User user, User friend,ConnectionStatus connectionStatus){
        this.user = user;
        this.friend = friend;
        this.connectionStatus = connectionStatus;
    }
}
