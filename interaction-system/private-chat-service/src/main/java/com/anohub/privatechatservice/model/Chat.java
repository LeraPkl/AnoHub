package com.anohub.privatechatservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@Document(collection = "chats")
public class Chat {

    @Id
    private String id;

    private String user1;

    private String user2;

    private String user2Nickname;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime expiresAt;

    private Boolean saveChat;

    public Chat() {
        user2Nickname = "user" + user2;
        expiresAt = LocalDateTime
                .now(ZoneOffset.UTC)
                .plusHours(24);
    }
}

