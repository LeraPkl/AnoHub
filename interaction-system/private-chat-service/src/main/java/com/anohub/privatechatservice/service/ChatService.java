package com.anohub.privatechatservice.service;

import com.anohub.privatechatservice.model.Chat;
import com.anohub.privatechatservice.repository.ChatRepository;
import com.anohub.privatechatservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public Mono<Chat> createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Mono<Void> deleteById(String id) {
        return chatRepository.findById(id)
                .flatMap(chat -> messageRepository.deleteAllByChatId(new ObjectId(chat.getId()))
                        .then(chatRepository.delete(chat)));
    }

    public Flux<Void> deleteByExpiresAt(LocalDateTime time) {
        return chatRepository.getByExpiresAtBefore(time)
                .flatMap(chat -> deleteById(chat.getId()));
    }

    public Flux<Chat> findBySenderId(String senderId) {
        return chatRepository.findBySenderId(senderId);
    }

    public Flux<Chat> findByReceiverId(String receiverId) {
        return chatRepository.findByReceiverId(receiverId);
    }
}
