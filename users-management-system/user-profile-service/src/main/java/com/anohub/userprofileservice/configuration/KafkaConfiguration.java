package com.anohub.userprofileservice.configuration;

import com.anohub.userprofileservice.model.UserProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public KafkaSender<String, UserProfile> kafkaSender(ProducerFactory<String, UserProfile> producerFactory) {
        Map<String, Object> props = producerFactory.getConfigurationProperties();
        SenderOptions<String, UserProfile> senderOptions = SenderOptions.create(props);
        return KafkaSender.create(senderOptions);
    }

}
