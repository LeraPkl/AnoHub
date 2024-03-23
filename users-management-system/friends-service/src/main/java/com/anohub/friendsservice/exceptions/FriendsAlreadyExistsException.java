package com.anohub.friendsservice.exceptions;

import static java.lang.String.format;

public class FriendsAlreadyExistsException extends RuntimeException {

    public FriendsAlreadyExistsException(String senderId, String receiverId) {
        super(format("Friend request already sent or users with id %s and %s are already friends",
                senderId, receiverId));
    }
}
