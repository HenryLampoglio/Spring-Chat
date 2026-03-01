package com.example.SpringChat.core.enums;

public enum ChatAccessType {
    PUBLIC("public"),
    PRIVATE("private"),
    INVITE_ONLY("invite_only");

    private final String value;

    ChatAccessType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ChatAccessType fromString(String text) {
        for (ChatAccessType b : ChatAccessType.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Tipo de acesso desconhecido: " + text);
    }

    @Override
    public String toString() {
        return this.value;
    }
}