package com.anohub.privatechatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
//@Import(KafkaConfiguration.class)
@SpringBootApplication
public class PrivateChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrivateChatServiceApplication.class, args);
    }

}
