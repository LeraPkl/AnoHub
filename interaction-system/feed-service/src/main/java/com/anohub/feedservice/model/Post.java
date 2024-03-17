package com.anohub.feedservice.model;

import com.anohub.feedservice.model.dto.UserDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    private String topicId;

    private Long userId;

    private String content;

    private PrivacyLevel privacyLevel = PrivacyLevel.PUBLIC;

    @Transient
    private Topic topic;

    @Transient
    private UserDto user;

    enum PrivacyLevel {
        PUBLIC,
        FOR_FRIENDS
    }
}
