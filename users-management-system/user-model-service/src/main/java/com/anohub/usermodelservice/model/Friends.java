package com.anohub.usermodelservice.model;

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

    private Long user1Id;

    private Long user2Id;

    private Boolean isAccepted;
}

