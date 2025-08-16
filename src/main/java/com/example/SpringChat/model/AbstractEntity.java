package com.example.SpringChat.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    protected ID id;

    @CreationTimestamp
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    protected LocalDateTime updatedAt;
}