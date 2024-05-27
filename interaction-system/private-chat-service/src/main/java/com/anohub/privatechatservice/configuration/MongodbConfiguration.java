package com.anohub.privatechatservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class MongodbConfiguration {


    @Bean
    public ReactiveTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTransactionManager(factory);
    }

    @Bean
    TransactionalOperator transactionalOperator(ReactiveTransactionManager txManager) {
        return TransactionalOperator.create(txManager);
    }

//    @Bean
//    public CryptVault cryptVault() {
//        // Configure your encryption keys here
//        return new CryptVault()
//                .with256BitAesCbcPkcs5PaddingAnd128BitSaltKey(0, oldKey)
//                .with256BitAesCbcPkcs5PaddingAnd128BitSaltKey(1, secretKey)
//                .withDefaultKeyVersion(1);
//    }
//
//    @Bean
//    public CachedEncryptionEventListener encryptionEventListener(CryptVault cryptVault) {
//        return new CachedEncryptionEventListener(cryptVault);
//    }

}

