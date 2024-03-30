package com.anohub.kafkaservice.event;

public interface SagaStep<T> {
    void process(T event);

    void rollback();
}
