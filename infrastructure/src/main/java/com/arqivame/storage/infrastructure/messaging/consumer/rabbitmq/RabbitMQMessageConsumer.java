package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq;

import java.io.Serializable;
import java.util.Set;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.arqivame.storage.infrastructure.messaging.consumer.MessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public abstract class RabbitMQMessageConsumer<T extends Serializable> extends MessageConsumer<Message<T>> {

    protected RabbitMQMessageConsumer(
            final Integer maxRetryAttempts,
            final MessageProducer<Message<T>> errorMessageProducer,
            final Set<Class<? extends Throwable>> unretryableExceptions) {
        super(new RabbitMQMessageConsumer.RabbitMQFailureHandler<T>(
                maxRetryAttempts,
                unretryableExceptions,
                errorMessageProducer));
    }

    static class RabbitMQFailureHandler<T extends Serializable> implements FailureHandler<Message<T>> {

        private final Integer maxRetryAttempts;
        private final Set<Class<? extends Throwable>> unretryableExceptions;

        private final MessageProducer<Message<T>> errorMessageProducer;

        RabbitMQFailureHandler(
                final Integer maxRetryAttempts,
                final Set<Class<? extends Throwable>> unretryableExceptions,
                final MessageProducer<Message<T>> errorMessageProducer) {
            this.maxRetryAttempts = maxRetryAttempts;
            this.unretryableExceptions = unretryableExceptions;
            this.errorMessageProducer = errorMessageProducer;
        }

        @Override
        public void handle(final Message<T> message, final Throwable throwable) {

            if (isMaxRetryAttemptsExceeded(getRetryCount(message.getHeaders())))
                errorMessageProducer.produce(message);

            if (isExceptionRetryable(throwable))
                throw RetryableException.of(throwable);

            errorMessageProducer.produce(message);
        }

        private Boolean isMaxRetryAttemptsExceeded(final Integer actualRetryCount) {
            return maxRetryAttempts.compareTo(actualRetryCount) <= 0;
        }

        private Boolean isExceptionRetryable(final Throwable throwable) {
            return !unretryableExceptions.contains(throwable.getClass());
        }

        private static Integer getRetryCount(final MessageHeaders headers) {
            // TODO : implementar de acordo com o rabbitmq utilizado
            return 1;
        }

    }

}
