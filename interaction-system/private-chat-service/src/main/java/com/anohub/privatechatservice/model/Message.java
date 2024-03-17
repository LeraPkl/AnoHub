package com.anohub.privatechatservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    private ObjectId chatId;

    private String senderId;

    private String content;

    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime sentAt = LocalDateTime.now(ZoneOffset.UTC);

    private Boolean isSeen;
}


