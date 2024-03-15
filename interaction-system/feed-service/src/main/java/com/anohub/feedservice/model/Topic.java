package com.anohub.feedservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "topics")
public class Topic {

    @Id
    private String id;

    private String categoryId;

    private String name;

}
