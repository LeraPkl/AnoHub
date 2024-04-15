package com.anohub.privatechatservice.model;

public record SendMessageRequest(String receiverId,
                                 String senderId,
                                 String content) {

}
