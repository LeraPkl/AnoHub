package com.anohub.privatechatservice.service;

import com.anohub.privatechatservice.model.Chat;
import com.anohub.privatechatservice.repository.ChatRepository;
import com.anohub.privatechatservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final TransactionalOperator transactionalOperator;

    public Mono<Chat> createChat(Chat chat) {
        return chatRepository.save(chat)
                .as(transactionalOperator::transactional);
    }

    public Mono<Void> deleteById(String id) {
        return chatRepository.findById(id)
                .flatMap(chat -> messageRepository.deleteAllByChatId(new ObjectId(chat.getId()))
                        .then(chatRepository.delete(chat)))
                .as(transactionalOperator::transactional);
    }

    public Flux<Void> deleteByExpiresAt(LocalDateTime time) {
        return chatRepository.getByExpiresAtBefore(time)
                .flatMap(chat -> deleteById(chat.getId()))
                .as(transactionalOperator::transactional);
    }

    public Flux<Chat> findByUser1(String user1) {
        return chatRepository.findByUser1(user1)
                .as(transactionalOperator::transactional);
    }

    public Mono<Chat> findChatByUsersId(String user1, String user2) {
        return chatRepository.findByUser1AndUser2(user1, user2)
                .as(transactionalOperator::transactional);
    }
}
