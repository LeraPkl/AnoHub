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
        return chatService.deleteById(id);
    }

    @GetMapping("/user1/{user1}")
    public Flux<Chat> findAllChatsByUserId(@PathVariable String user1) {
        return chatService.findByUser1(user1);
    }

    @GetMapping("/user1/{user1}/{user2}")
    public Mono<Chat> findChatsByUsersId(@PathVariable String user1,
                                         @PathVariable String user2) {
        return chatService.findChatByUsersId(user1, user2);
    }

}
