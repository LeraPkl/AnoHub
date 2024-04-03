package com.anohub.usermodelservice.event.creation;

import com.anohub.usermodelservice.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationFailedEvent implements BaseEvent {
    private UUID userId;
}
