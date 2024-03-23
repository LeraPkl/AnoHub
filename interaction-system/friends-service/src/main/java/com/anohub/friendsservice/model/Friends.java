package com.anohub.friendsservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Builder
@Table("friends")
public class Friends {

    @Id
    private Long id;

    private String user1Id;

    private String user2Id;

    private Boolean isAccepted;
}

