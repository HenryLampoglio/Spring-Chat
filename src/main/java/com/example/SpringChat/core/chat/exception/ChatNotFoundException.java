package com.example.SpringChat.core.chat.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(String chatId) {
        super("Chat não encontrado com o ID: " + chatId);
    }
}
