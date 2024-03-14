package com.anohub.usermodelservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Setter
@Getter
public class User {

    @Id
    private Long id;

    private String username;

    private String password;

}

