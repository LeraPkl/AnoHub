package com.anohub.friendsservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table("friends")
public class Friends {

    @Column("user1_id")
    private Long user1Id;

    @Column("user2_id")
    private Long user2Id;

    private Boolean isAccepted;

    public void setId(FriendsPK compositeId) {
        var user2 = compositeId.getUser2Id();
        var user1 = compositeId.getUser1Id();
        user1Id = user1.compareTo(user2) < 0 ? user1 : user2;
        user2Id = user1.compareTo(user2) < 0 ? user2 : user1;
    }
}

