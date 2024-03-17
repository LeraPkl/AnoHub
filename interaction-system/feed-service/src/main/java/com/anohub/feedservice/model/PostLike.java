package com.anohub.feedservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "post_likes")
public class PostLike {

    @Id
    private String id;

    @Indexed
    private String postId;

    @Indexed
    private String userId;

    private Boolean isLike;
}
