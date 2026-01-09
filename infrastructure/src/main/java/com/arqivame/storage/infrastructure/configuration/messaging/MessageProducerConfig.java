package com.arqivame.storage.infrastructure.configuration.messaging;

import java.util.Objects;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.arqivame.storage.infrastructure.file.model.FileUploadSessionAbortedMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionClosedMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCompletedMessage;
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
    MessageProducer<FileUploadSessionCompletedMessage> fileUploadSessionCompletedEvent() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionCompletedEvent-out-0");
    }

    @Bean
    MessageProducer<FileUploadSessionCompletedMessage> fileUploadSessionCompletedEventError() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionCompletedEventError-out-0");
    }

    @Bean
    @Primary
    MessageProducer<FileUploadSessionClosedMessage> fileUploadSessionClosedEvent() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionClosedEvent-out-0");
    }

    @Bean
    MessageProducer<FileUploadSessionClosedMessage> fileUploadSessionClosedEventError() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionClosedEventError-out-0");
    }

    @Bean
    @Primary
    MessageProducer<FileUploadSessionAbortedMessage> fileUploadSessionAbortedEvent() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionAbortedEvent-out-0");
    }

    @Bean
    MessageProducer<FileUploadSessionAbortedMessage> fileUploadSessionAbortedEventError() {
        return new SpringCloudMessageProducer<>(streamBridge, "fileUploadSessionAbortedEventError-out-0");
    }

}
