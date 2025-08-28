package com.example.SpringChat.core.user.entity;

import com.example.SpringChat.core.AbstractEntity;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class User extends AbstractEntity<UUID> {
    private String nickname;
    private String email;
    private String hashedPassword;
    private int publicIdentificationKey;

    private String userQuote;

    public User(String nickname, String email, String hashedPassword, int publicIdentificationKey) {
        this.nickname = nickname;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.publicIdentificationKey = publicIdentificationKey;
    }
}
