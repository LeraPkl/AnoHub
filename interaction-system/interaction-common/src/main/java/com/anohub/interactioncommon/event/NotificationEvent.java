package com.anohub.interactioncommon.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {

    protected Long to;

    protected String message;

}
