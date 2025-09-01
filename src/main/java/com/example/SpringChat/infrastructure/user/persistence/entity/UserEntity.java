package com.example.SpringChat.infrastructure.user.persistence.entity;

import com.example.SpringChat.core.user.entity.User;
import com.example.SpringChat.infrastructure.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity extends AbstractEntity<UUID> implements UserDetails {

    @NotBlank
    private String nickname;

    @Email
    private String email;

    @Column(name = "password_hash") // Renomeado para clareza
    private String hashedPassword;

    private Integer publicIdentificationKey;
    private String userQuote;

    @ManyToMany(fetch = FetchType.LAZY) // EAGER pode causar problemas de performance
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id") // Nome corrigido
    )
    private Set<UserEntity> friends = new HashSet<>();

    public UserEntity(User user){
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.hashedPassword = user.getHashedPassword();
        this.publicIdentificationKey = user.getPublicIdentificationKey();
        this.userQuote = user.getUserQuote();
    }

    public User toCoreUser() {
        User coreUser = new User(this.nickname, this.email, this.hashedPassword, this.publicIdentificationKey);
        coreUser.setId(this.id);
        coreUser.setCreatedAt(this.getCreatedAt());
        coreUser.setUpdatedAt(this.getUpdatedAt());
        coreUser.setUserQuote(this.userQuote);
        return coreUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        // CORREÇÃO: Retorna o campo da senha criptografada
        return this.hashedPassword;
    }

    @Override
    public String getUsername() {
        // CORREÇÃO: Retorna o identificador único do usuário (o e-mail)
        return this.email;
    }
}