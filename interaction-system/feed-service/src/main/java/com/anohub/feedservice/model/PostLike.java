package com.anohub.feedservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
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
