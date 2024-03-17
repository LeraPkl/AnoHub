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

    private String senderId;

    private String receiverId;

    private String receiverNickname;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime expiresAt;

    private Boolean saveChat;

    public Chat() {
        receiverNickname = "user" + receiverId;
        expiresAt = LocalDateTime
                .now(ZoneOffset.UTC)
                .plusHours(24);
    }
}

