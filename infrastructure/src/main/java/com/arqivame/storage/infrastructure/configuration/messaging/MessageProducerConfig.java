package com.arqivame.storage.infrastructure.configuration.messaging;

import java.util.Objects;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.file.event.FileUploadSessionCanceledEvent;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;
import com.arqivame.storage.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;

@Configuration
public class MessageProducerConfig {

    private final StreamBridge streamBridge;

    public MessageProducerConfig(final StreamBridge streamBridge) {
        this.streamBridge = Objects.requireNonNull(streamBridge);
    }

    @Bean
    MessageProducer<Event<FileUploadSessionCanceledEvent.Data>> fileUploadSessionCanceledEvent() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionCanceledEvent-out-0");
    }

    @Bean
    MessageProducer<Message<FileUploadSessionCanceledEvent>> fileUploadSessionCanceledEventError() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionCanceledEventError-out-0");
    }

}
