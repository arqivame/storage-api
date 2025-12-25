package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq;

import java.io.Serializable;
import java.util.Set;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.arqivame.storage.infrastructure.messaging.consumer.MessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public abstract class RabbitMQMessageConsumer<T extends Serializable> extends MessageConsumer<Message<T>> {

    protected RabbitMQMessageConsumer(final RabbitMQFailureHandler<T> failureHandler) {
        super(failureHandler);
    }

    public static class RabbitMQFailureHandler<T extends Serializable> implements FailureHandler<Message<T>> {

        private final Integer maxRetryAttempts;
        private final Set<Class<? extends Throwable>> unretryableExceptions;

        private final MessageProducer<T> errorMessageProducer;

        public RabbitMQFailureHandler(
                final Integer maxRetryAttempts,
                final Set<Class<? extends Throwable>> unretryableExceptions,
                final MessageProducer<T> errorMessageProducer) {
            this.maxRetryAttempts = maxRetryAttempts;
            this.unretryableExceptions = unretryableExceptions;
            this.errorMessageProducer = errorMessageProducer;
        }

        @Override
        public void handle(final Message<T> message, final Throwable throwable) {

            if (isMaxRetryAttemptsExceeded(getRetryCount(message.getHeaders())))
                errorMessageProducer.produce(message.getPayload());

            if (isExceptionRetryable(throwable))
                throw RetryableException.of(throwable);

            errorMessageProducer.produce(message.getPayload());
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
