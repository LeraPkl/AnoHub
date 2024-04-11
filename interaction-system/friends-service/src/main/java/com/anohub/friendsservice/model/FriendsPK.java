package com.anohub.friendsservice.model;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class FriendsPK implements Serializable {
    Long user1Id;
    Long user2Id;
}
