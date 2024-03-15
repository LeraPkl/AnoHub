package com.anohub.privatechatservice.model;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "chats")
public class Chat {

    @Id
    private String id;

    private String senderId;

    private String receiverId;

    private String receiverNickname;

    private LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

    @PostConstruct
    public void init() {
        this.receiverNickname = "user" + receiverId;
    }
}

