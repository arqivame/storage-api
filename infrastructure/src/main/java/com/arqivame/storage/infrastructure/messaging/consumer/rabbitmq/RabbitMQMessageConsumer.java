package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.arqivame.storage.infrastructure.messaging.consumer.MessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public abstract class RabbitMQMessageConsumer<T extends Serializable> extends MessageConsumer<Message<T>> {

    protected RabbitMQMessageConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<Message<T>> errorMessageProducer,
            final Set<Class<? extends Throwable>> unretryableExceptions) {
        super(new RabbitMQMessageConsumer.RabbitMQFailureHandler<T>(
                maxRetryAttempts,
                unretryableExceptions,
                errorMessageProducer));
    }

    static class RabbitMQFailureHandler<T extends Serializable> implements FailureHandler<Message<T>> {

        private final Long maxRetryAttempts;
        private final Set<Class<? extends Throwable>> unretryableExceptions;

        private final MessageProducer<Message<T>> errorMessageProducer;

        RabbitMQFailureHandler(
                final Long maxRetryAttempts,
                final Set<Class<? extends Throwable>> unretryableExceptions,
                final MessageProducer<Message<T>> errorMessageProducer) {
            this.maxRetryAttempts = maxRetryAttempts;
            this.unretryableExceptions = unretryableExceptions;
            this.errorMessageProducer = errorMessageProducer;
        }

        @Override
        public void handle(final Message<T> message, final Throwable throwable) {

            if (isMaxRetryAttemptsExceeded(getRetryCount(message.getHeaders()))) {
                errorMessageProducer.produce(message);
                return;
            }

            if (isExceptionRetryable(throwable))
                throw RetryableException.of(throwable);

            errorMessageProducer.produce(message);
        }

        private Boolean isMaxRetryAttemptsExceeded(final Long actualRetryCount) {
            return maxRetryAttempts.compareTo(actualRetryCount) <= 0;
        }

        private Boolean isExceptionRetryable(final Throwable throwable) {
            return !unretryableExceptions.contains(throwable.getClass());
        }

        @SuppressWarnings("unchecked")
        private static Long getRetryCount(final MessageHeaders headers) {

            final List<Map<String, Object>> xDeath = (List<Map<String, Object>>) headers.get("x-death");
            if (xDeath != null && !xDeath.isEmpty()) {
                final Map<String, Object> deathInfo = xDeath.get(0);
                final Long count = (Long) deathInfo.get("count");
                return count != null ? count : 0L;
            }

            return 0L;
        }

    }

}
