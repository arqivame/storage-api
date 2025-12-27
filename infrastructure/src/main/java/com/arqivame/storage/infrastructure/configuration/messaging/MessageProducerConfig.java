package com.arqivame.storage.infrastructure.configuration.messaging;

import java.util.Objects;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCanceledMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionMarkedForDeletionMessage;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;
import com.arqivame.storage.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;

@Configuration
public class MessageProducerConfig {

    private final StreamBridge streamBridge;

    public MessageProducerConfig(final StreamBridge streamBridge) {
        this.streamBridge = Objects.requireNonNull(streamBridge);
    }

    @Bean
    @Primary
    MessageProducer<FileUploadSessionCanceledMessage> fileUploadSessionCanceledEvent() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionCanceledEvent-out-0");
    }

    @Bean
    MessageProducer<FileUploadSessionCanceledMessage> fileUploadSessionCanceledEventError() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionCanceledEventError-out-0");
    }

    @Bean
    @Primary
    MessageProducer<FileUploadSessionMarkedForDeletionMessage> fileUploadSessionMarkedForDeletionEvent() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionMarkedForDeletionEvent-out-0");
    }

    @Bean
    MessageProducer<FileUploadSessionMarkedForDeletionMessage> fileUploadSessionMarkedForDeletionEventError() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionMarkedForDeletionEventError-out-0");
    }

}
