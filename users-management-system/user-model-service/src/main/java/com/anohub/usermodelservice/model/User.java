package com.anohub.usermodelservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Table("users")
public class User {

    @Id
    private Long id;

    @Size(min = 4, max = 50)
    private String username;

    @NotBlank
    private String password;

    private List<User> friends = new ArrayList<>();

}

