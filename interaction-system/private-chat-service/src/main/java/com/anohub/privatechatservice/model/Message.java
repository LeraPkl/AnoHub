package com.anohub.privatechatservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    private String chatId;

    private String senderId;

    private String content;

    @CreatedDate
    private LocalDateTime sentAt;

}

