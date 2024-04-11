package com.anohub.interactioncommon.event;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLikeNotificationEvent extends NotificationEvent {

    private String postTitle;

    public PostLikeNotificationEvent(String postTitle, Long to, String message) {
        super(to, message);
        this.postTitle = postTitle;
    }
}
