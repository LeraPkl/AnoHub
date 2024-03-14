package com.anohub.feedservice.model;

import com.anohub.feedservice.model.dto.UserDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    @DBRef
    private Topic topic;

    private Long userId;

    private String content;

    @Transient
    private UserDto user;

}
