package com.anohub.friendsservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table("friends")
public class Friends {

    @Id
    private String id;

    private Boolean isAccepted;

    public void setId(String userId1, String userId2) {
        if (userId1.compareTo(userId2) < 0) {
            id = userId1 + '_' + userId2;
        } else {
            id = userId2 + '_' + userId1;
        }
    }
}

