package com.anohub.privatechatservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Encrypted;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    private String receiverId;

    private String senderId;

    private String chatId;

    @Encrypted
    private String content;

    @CreatedDate
    @Builder.Default
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime sentAt = LocalDateTime.now(ZoneOffset.UTC);

    @Builder.Default
    private Boolean isSeen = false;
}

