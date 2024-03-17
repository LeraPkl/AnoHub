package com.anohub.privatechatservice.controller;

import com.anohub.privatechatservice.model.Message;
import com.anohub.privatechatservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private-chat-service/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public Mono<Message> sendMessage(@RequestBody Message message) {
        return messageService.sendMessage(message);
    }

    @GetMapping("/chat/{chatId}")
    public Flux<Message> getAllMessagesByChatId(@PathVariable ObjectId chatId) {
        return messageService.getAllMessagesByChatId(chatId);
    }

    @PutMapping("/{id}")
    public Mono<Message> updateMessage(@PathVariable String id, @RequestBody String content) {
        return messageService.updateMessage(id, content);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMessage(@PathVariable String id) {
        return messageService.deleteMessage(id);
    }
}
