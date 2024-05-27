package com.anohub.privatechatservice.controller;

import com.anohub.privatechatservice.model.Message;
import com.anohub.privatechatservice.model.SendMessageRequest;
import com.anohub.privatechatservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private-chat-service/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/send")
    @SendTo("/queue/messages")
    public Mono<Message> sendMessage(@RequestBody SendMessageRequest message) {
        return messageService.sendMessage(message);
    }

    @GetMapping("/chat/{chatId}")
    public Flux<Message> getMessagesByChatIdPage(@PathVariable String chatId,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "50") int size) {
        return messageService.getMessagesByChatIdPage(chatId, page, size);
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
