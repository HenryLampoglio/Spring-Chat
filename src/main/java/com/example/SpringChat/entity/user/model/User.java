package com.example.SpringChat.entity.user.model;

import com.example.SpringChat.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends AbstractEntity<UUID> {
    private UUID id;
    private String nickname;
    private String email;
    private String HashedPassword;
    private Integer publicIdentificationKey;
    private String userQuote;
}
