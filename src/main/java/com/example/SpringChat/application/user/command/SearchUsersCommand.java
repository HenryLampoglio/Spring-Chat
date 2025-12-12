package com.example.SpringChat.application.user.command;

import lombok.Getter;

import java.util.UUID;

public class SearchUsersCommand {
    private final String userIdentifier;
    @Getter
    private final UUID userId;

    public SearchUsersCommand(String userIdentifier, UUID userId){
        this.userIdentifier = userIdentifier;
        this.userId = userId;
    }

    public String getNicknamePart(){
        int index = getSeparatorIndex();
        return (index == -1) ? this.userIdentifier : this.userIdentifier.substring(0, index);
    }

    public Integer getKeyPart(){
        int index = getSeparatorIndex();
        return (index == -1) ? 0: Integer.parseInt(this.userIdentifier.substring(index + 1));
    }

    private int getSeparatorIndex() {
        if(this.userIdentifier == null){
            return -1;
        }
        return this.userIdentifier.indexOf('#');
    }
}
