package com.anohub.feedservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "reposted_posts")
public class RepostedPost {

    @Id
    private String id;

    private String postId;
    
    private String userProfileId;

}
