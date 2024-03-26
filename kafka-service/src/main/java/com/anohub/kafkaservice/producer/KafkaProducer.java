package com.anohub.kafkaservice.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    public void produce(String topicName, T event) {

        CompletableFuture<SendResult<String, T>> future = kafkaTemplate.send(topicName, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Unable to send message=[{}] due to : {}", event, ex.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } else {
                log.info("successfully executed event {} to topic {}",
                        event.getClass().getName(),
                        result.getRecordMetadata().topic());
            }
        });
    }
}


