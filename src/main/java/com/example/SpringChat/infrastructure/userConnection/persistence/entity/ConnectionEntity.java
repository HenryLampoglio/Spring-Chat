package com.example.SpringChat.infrastructure.userConnection.persistence.entity;

import com.example.SpringChat.core.connection.entity.Connection;
import com.example.SpringChat.core.enums.ConnectionStatus;
import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.AbstractEntity;
import com.example.SpringChat.infrastructure.user.persistence.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private UserEntity requester;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    @NotNull
    @Column(name = "connection_status", columnDefinition = "conn_type")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ConnectionStatus connectionStatus;

    public ConnectionEntity(Connection connection){
        this.id = connection.getId();
        this.connectionStatus = connection.getConnectionStatus();
        this.requester = mapUser(connection.getRequester());
        this.receiver = mapUser(connection.getReceiver());
    }

    public Connection toCoreConnection() {
        Connection coreConnection = new Connection();
        coreConnection.setId(this.id);
        coreConnection.setConnectionStatus(this.connectionStatus);

        if(this.requester != null) coreConnection.setRequester((this.requester.toCoreUser()));
        if(this.receiver != null) coreConnection.setReceiver((this.receiver.toCoreUser()));

        return coreConnection;
    }

    private UserEntity mapUser(User user) {
        if(user == null || user.getId() == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId((user.getId()));
        return entity;
    }
}
