package com.anohub.friendsservice.model;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class FriendsPK implements Serializable {
    String user1Id;
    String user2Id;
}
