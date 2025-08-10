package com.example.SpringChat.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private @GeneratedValue @Id Long id;
    private String nickname;
    private String email;
    private String hash_password;
    private Integer public_identification_key;
    private String user_quote;

}
