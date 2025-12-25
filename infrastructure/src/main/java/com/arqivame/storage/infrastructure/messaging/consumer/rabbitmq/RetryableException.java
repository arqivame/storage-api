package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq;

public class RetryableException extends RuntimeException {

    private RetryableException(final Throwable cause) {
        super(cause);
    }

    public static RetryableException of(final Throwable cause) {
        return new RetryableException(cause);
    }

}
