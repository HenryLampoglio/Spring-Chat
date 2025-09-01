package com.example.SpringChat.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID; // Importação correta da classe UUID

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

    @Id
    @GeneratedValue
    @UuidGenerator
    protected UUID id; // Agora o tipo é java.util.UUID, não o tipo genérico

    @CreationTimestamp
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    protected LocalDateTime updatedAt;
}