package com.arqivame.storage.infrastructure.message.consumer;

import java.io.Serializable;
import java.util.function.Consumer;

@FunctionalInterface
public interface MessageConsumer<T extends Serializable> extends Consumer<T> {

    void consume(T message);

    default void accept(final T message) {
        consume(message);
    }

}
