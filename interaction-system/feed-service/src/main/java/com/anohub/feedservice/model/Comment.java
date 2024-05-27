package com.anohub.feedservice.model;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private String postId;

    @Nullable
    private String commentId;

    private String userId;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @Transient
    List<Comment> replies = new ArrayList<>();
}