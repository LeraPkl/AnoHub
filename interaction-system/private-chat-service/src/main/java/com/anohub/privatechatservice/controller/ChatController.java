package com.anohub.privatechatservice.controller;

import com.anohub.privatechatservice.model.Chat;
import com.anohub.privatechatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @DeleteMapping("/{user1}/{user2}")
    public Mono<Void> deleteChatByUsersId(@PathVariable String user1,
                                          @PathVariable String user2) {
        return chatService.deleteByUsersId(user1, user2);
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

    @PutMapping("/{chatId}/assign-nickname")
    public Mono<ResponseEntity<Chat>> assignNickname(@PathVariable String chatId,
                                                     @RequestParam String userId,
                                                     @RequestParam String nickname) {
        return chatService.assignNickname(chatId, userId, nickname)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
