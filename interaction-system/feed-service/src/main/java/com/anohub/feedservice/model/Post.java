package com.anohub.feedservice.model;

import com.anohub.feedservice.model.dto.UserDto;
import com.mongodb.lang.Nullable;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    private String title;

    private String topicId;

    private String userProfileId;

    @Length(max = 1000, message = "The content must not exceed 1000 characters")
    private String content;

    @Nullable
    private Poll poll;

    @Builder.Default
    private PrivacyLevel privacyLevel = PrivacyLevel.PUBLIC;

    @CreatedDate
    private LocalDateTime createdAt;

    @Transient
    private Double popularity;

    @Transient
    private Topic topic;

    @Transient
    private UserDto user;

    enum PrivacyLevel {
        PUBLIC,
        FOR_FRIENDS
    }
}


