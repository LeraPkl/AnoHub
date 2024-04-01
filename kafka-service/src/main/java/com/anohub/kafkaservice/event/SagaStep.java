package com.anohub.kafkaservice.event;

import reactor.core.publisher.Mono;

public interface SagaStep<T, U> {
    Mono<Void> process(T event);

    Mono<Void> rollback(U event);
}
