package com.anohub.usermodelservice.event.deletion;

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
public class DeleteUserEventFailedEvent implements BaseEvent {
    private UUID userId;
}
