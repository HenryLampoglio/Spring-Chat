package com.example.SpringChat.entity.user.gateway;
import java.util.List;

public interface FriendshipGatewayPort {
    List<User> findFriendsByUserId(String userId);
}