package com.anohub.privatechatservice.configuration;

import com.anohub.interactioncommon.event.FriendRequestAcceptedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;

@Configuration
public class ConsumerConfiguration {
    @Bean
    public ReceiverOptions<String, FriendRequestAcceptedEvent> kafkaReceiverOptions(
            @Value(value = "${kafka.topics.friend-request-accepted}") String topic,
            SslBundles ssl,
            KafkaProperties kafkaProperties) {
        ReceiverOptions<String, FriendRequestAcceptedEvent> basicReceiverOptions =
                ReceiverOptions.create(kafkaProperties.buildConsumerProperties(ssl));
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, FriendRequestAcceptedEvent> reactiveKafkaConsumerTemplate(
            ReceiverOptions<String, FriendRequestAcceptedEvent> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }
}
