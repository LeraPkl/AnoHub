package com.anohub.configserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
@Slf4j
public class ConfigServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

    @Value("${GIT_PRIVATE_KEY}")
    private String privateKey;

    @Override
    public void run(String... args) throws Exception {
        log.info("pk is: {}", privateKey);
    }
}

