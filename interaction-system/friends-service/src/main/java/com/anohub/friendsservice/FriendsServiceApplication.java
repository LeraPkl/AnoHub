package com.anohub.friendsservice;

import com.anohub.kafkaservice.configuration.KafkaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories
@EnableR2dbcAuditing
@Import(KafkaConfiguration.class)
public class FriendsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendsServiceApplication.class, args);
    }

}
