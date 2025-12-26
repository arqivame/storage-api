package com.arqivame.storage.infrastructure.messaging.producer.springcloud;

import java.util.Objects;

import org.springframework.cloud.stream.function.StreamBridge;

import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public class SpringCloudMessageProducer<T> implements MessageProducer<T> {

    private final StreamBridge streamBridge;
    private final String bindingName;

    public SpringCloudMessageProducer(
            final StreamBridge streamBridge,
            final String bindingName) {
        this.streamBridge = Objects.requireNonNull(streamBridge);
        this.bindingName = Objects.requireNonNull(bindingName);
    }

    @Override
    public void produce(final T payload) {
        streamBridge.send(bindingName, payload);
    }

}