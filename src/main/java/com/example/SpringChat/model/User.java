package com.example.SpringChat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity<UUID> {

    @NotBlank
    private String nickname;

    @Email
    private String email;

    @Column(name = "hash_password")
    private String hashedPassword;

    private Integer publicIdentificationKey;
    private String userQuote;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();
}
