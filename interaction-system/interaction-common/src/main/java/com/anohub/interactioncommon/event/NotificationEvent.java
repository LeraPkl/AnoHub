package com.anohub.interactioncommon.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class NotificationEvent {

    protected Long to;

    protected String message;

}
