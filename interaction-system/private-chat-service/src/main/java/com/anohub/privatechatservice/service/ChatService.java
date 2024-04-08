package com.anohub.privatechatservice.service;

import com.anohub.interactioncommon.event.FriendRequestAcceptedEvent;
import com.anohub.privatechatservice.model.Chat;
import com.anohub.privatechatservice.repository.ChatRepository;
import com.anohub.privatechatservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final TransactionalOperator transactionalOperator;

    @KafkaListener(topics = "#{'${kafka.topics.friend-request-accepted}'}")
    public void consumeFriendRequestAcceptedEvent(FriendRequestAcceptedEvent event) {
        Chat chat = new Chat();
        chat.setUser1(new Chat.User(event.getFrom()));
        chat.setUser2(new Chat.User(event.getTo()));
        chatRepository.save(chat)
                .as(transactionalOperator::transactional)
                .log()
                .doOnNext(chat1 -> log.info("chat is saved: {}", chat1))
                .subscribe();
    }

    public Mono<Void> deleteByUsersId(String user1Id, String user2Id) {
        return chatRepository.findByUser1_IdAndUser2_Id(user1Id, user2Id)
                .flatMap(chat -> messageRepository.deleteAllByChatId(new ObjectId(chat.getId()))
                        .then(chatRepository.delete(chat)))
                .as(transactionalOperator::transactional);
    }

    public Flux<Void> deleteByExpiresAt(LocalDateTime time) {
        return chatRepository.findByExpiresAtBeforeAndSaveChatIsFalse(time)
                .flatMap(chat -> deleteById(chat.getId()))
                .as(transactionalOperator::transactional);
    }

    public Flux<Chat> findByUser1(String user1) {
        return chatRepository.findByUserId(user1)
                .as(transactionalOperator::transactional);
    }

    public Mono<Chat> findChatByUsersId(String user1, String user2) {
        return chatRepository.findByUser1_IdAndUser2_Id(user1, user2)
                .as(transactionalOperator::transactional);
    }

    public Mono<Chat> assignNickname(String user1Id, String user2Id, String user2Nickname) {
        return chatRepository.findByUser1_IdAndUser2_Id(user1Id, user2Id)
                .flatMap(chat -> {
                    if (chat.getUser1().getId().equals(user2Id)) {
                        chat.getUser1().setNickname(user2Nickname);
                    } else if (chat.getUser2().getId().equals(user2Id)) {
                        chat.getUser2().setNickname(user2Nickname);
                    }
                    return chatRepository.save(chat);
                });
    }

    private Mono<Void> deleteById(String id) {
        return chatRepository.findById(id)
                .flatMap(chat -> messageRepository.deleteAllByChatId(new ObjectId(chat.getId()))
                        .then(chatRepository.delete(chat)))
                .as(transactionalOperator::transactional);
    }
}
