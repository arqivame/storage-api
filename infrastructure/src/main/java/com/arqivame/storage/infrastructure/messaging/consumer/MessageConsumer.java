package com.arqivame.storage.infrastructure.messaging.consumer;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class MessageConsumer<T> implements Consumer<T> {

    private final Optional<FailureHandler<T>> failureHandler;

    protected MessageConsumer(final FailureHandler<T> failureHandler) {
        this.failureHandler = Optional.ofNullable(failureHandler);
    }

    // protected MessageConsumer() {
    // this.failureHandler = Optional.empty();
    // }

    @Override
    public void accept(final T message) {

        try {
            consume(message);
        } catch (Throwable throwable) {
            failureHandler.ifPresentOrElse(
                    handler -> handler.handle(message, throwable),
                    () -> {
                        throw throwable;
                    });
        }

    }

    public abstract void consume(final T message);

    @FunctionalInterface
    public interface FailureHandler<T> {

        void handle(final T message, final Throwable throwable);

    }

}
