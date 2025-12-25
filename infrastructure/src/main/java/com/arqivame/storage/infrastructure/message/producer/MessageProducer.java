package com.arqivame.storage.infrastructure.message.producer;

import java.io.Serializable;

@FunctionalInterface
public interface MessageProducer<T extends Serializable> {

    void produce(T message);

}
