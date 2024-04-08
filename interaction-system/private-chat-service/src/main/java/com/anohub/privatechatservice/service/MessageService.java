package com.anohub.privatechatservice.service;

import com.anohub.privatechatservice.model.Message;
import com.anohub.privatechatservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final TransactionalOperator transactionalOperator;

    public Mono<Message> sendMessage(Message message) {
        return messageRepository.save(message)
                .as(transactionalOperator::transactional);
    }

    public Flux<Message> getMessagesByChatIdPage(ObjectId chatId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sentAt"));
        return messageRepository.findAllByChatId(chatId, pageRequest)
                .as(transactionalOperator::transactional);
    }


    public Mono<Message> updateMessage(String id, String content) {
        return messageRepository.findById(id)
                .doOnSuccess(m -> m.setContent(content))
                .flatMap(messageRepository::save)
                .as(transactionalOperator::transactional);
    }

    public Mono<Void> deleteMessage(String id) {
        return messageRepository.deleteById(id)
                .as(transactionalOperator::transactional);
    }
}
