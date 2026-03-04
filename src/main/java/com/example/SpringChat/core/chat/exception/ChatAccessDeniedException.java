package com.example.SpringChat.core.chat.exception;

public class ChatAccessDeniedException extends RuntimeException {
    public ChatAccessDeniedException(String chatId) {
        super("Usuário não tem acesso ao chat: " + chatId);
    }
}
