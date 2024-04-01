package com.anohub.authenticationservice.configuration;

import com.anohub.usermodelservice.event.DeleteUserEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public KafkaSender<String, DeleteUserEvent> kafkaSender(
            ProducerFactory<String, DeleteUserEvent> producerFactory) {
        Map<String, Object> props = producerFactory.getConfigurationProperties();
        SenderOptions<String, DeleteUserEvent> senderOptions = SenderOptions.create(props);
        return KafkaSender.create(senderOptions);
    }

}
