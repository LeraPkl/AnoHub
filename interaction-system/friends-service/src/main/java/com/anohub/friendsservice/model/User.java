package com.anohub.friendsservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class User {

    private String id;

    private String email;

    private List<User> friends = new ArrayList<>();

}

