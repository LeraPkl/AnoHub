package com.anohub.privatechatservice.controller;

import com.anohub.privatechatservice.model.Chat;
import com.anohub.privatechatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private-chat-service/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public Mono<Chat> createChat(@RequestBody Chat chat) {
        return chatService.createChat(chat);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteChat(@PathVariable String id) {
        return chatService.deleteChat(id);
    }

    @GetMapping("/sender/{senderId}")
    public Flux<Chat> findBySenderId(@PathVariable String senderId) {
        return chatService.findBySenderId(senderId);
    }

    @GetMapping("/receiver/{receiverId}")
    public Flux<Chat> findByReceiverId(@PathVariable String receiverId) {
        return chatService.findByReceiverId(receiverId);
    }
}
