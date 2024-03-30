package com.anohub.authenticationservice.model;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String id;

    @Email
    private String email;

}
