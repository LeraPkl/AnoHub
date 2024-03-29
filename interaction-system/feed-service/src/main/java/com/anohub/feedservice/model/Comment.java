package com.anohub.feedservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private String postId;

    private String userId;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

}