package com.anohub.privatechatservice.service;

import com.anohub.privatechatservice.model.Message;
import com.anohub.privatechatservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Mono<Message> sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public Flux<Message> getAllMessagesByChatId(String chatId) {
        return messageRepository.findByChatId(chatId);
    }

    public Mono<Message> updateMessage(String id, String content) {
        return messageRepository.findById(id)
                .doOnSuccess(m -> m.setContent(content))
                .flatMap(messageRepository::save);
    }

    public Mono<Void> deleteMessage(String id) {
        return messageRepository.deleteById(id);
    }
}
