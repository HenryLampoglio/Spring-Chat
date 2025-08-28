// src/main/java/com/example/SpringChat/core/AbstractEntity.java

package com.example.SpringChat.core;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

    protected ID id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public AbstractEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}