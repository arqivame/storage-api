package com.arqivame.storage.infrastructure.messaging.producer;

@FunctionalInterface
public interface MessageProducer<T> {

    void produce(T payload);

}
