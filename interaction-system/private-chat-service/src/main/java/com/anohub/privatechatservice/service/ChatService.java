package com.anohub.privatechatservice.service;

import com.anohub.privatechatservice.model.Chat;
import com.anohub.privatechatservice.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Mono<Chat> createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Mono<Void> deleteChat(String id) {
        return chatRepository.deleteById(id);
    }


    public Flux<Chat> findBySenderId(String senderId) {
        return chatRepository.findBySenderId(senderId);
    }

    public Flux<Chat> findByReceiverId(String receiverId) {
        return chatRepository.findByReceiverId(receiverId);
    }
}
