package com.example.SpringChat.infrastructure.userConnection.persistence.entity;

import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.AbstractEntity;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "user_connections")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConnectionEntity extends AbstractEntity<UUID> {
    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private UserEntity friend;

    @NotBlank
    @Column(name = "connection_status", columnDefinition = "conn_type")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ConnectionStatus connectionStatus;

    public ConnectionEntity(Connection connection){
        this.id = connection.getId();
        this.connectionStatus = connection.getConnectionStatus();
        this.user = mapUser(connection.getUser());
        this.friend = mapUser(connection.getFriend());
    }

    public Connection toCoreConnection() {
        Connection coreConnection = new Connection();
        coreConnection.setId(this.id);
        coreConnection.setConnectionStatus(this.connectionStatus);

        if(this.user != null) coreConnection.setUser((this.user.toCoreUser()));
        if(this.friend != null) coreConnection.setFriend((this.friend.toCoreUser()));

        return coreConnection;
    }

    private UserEntity mapUser(User user) {
        if(user == null || user.getId() == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId((user.getId()));
        return entity;
    }
}
