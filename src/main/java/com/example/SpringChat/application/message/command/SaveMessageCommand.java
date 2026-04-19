package com.example.SpringChat.application.message.command;

import com.example.SpringChat.core.message.entity.Message;

import java.time.Instant;
import java.util.UUID;

public record SaveMessageCommand(Message message) {
}
