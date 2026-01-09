package com.arqivame.storage.infrastructure.configuration.messaging;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.arqivame.storage.application.service.file.ChunkCleanerService;
import com.arqivame.storage.application.service.file.FinalizeFileProcessingService;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionAbortedMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionClosedMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCompletedMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file.FileUploadSessionAbortedConsumer;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file.FileUploadSessionClosedConsumer;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file.FileUploadSessionCompletedConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

@Configuration
public class MessageConsumerConfig {

    @Bean
    Consumer<Message<FileUploadSessionCompletedMessage>> fileUploadSessionCompletedConsumer(
            @Value("${application.messaging.consumer.file-upload-session-completed-event.max-attempts}") final Long maxAttempts,
            @Qualifier("fileUploadSessionCompletedEventError") final MessageProducer<FileUploadSessionCompletedMessage> errorMessageProducer,
            final FinalizeFileProcessingService finalizeFileProcessingService) {
        return new FileUploadSessionCompletedConsumer(
                maxAttempts,
                errorMessageProducer,
                finalizeFileProcessingService);
    }

    @Bean
    Consumer<Message<FileUploadSessionClosedMessage>> fileUploadSessionClosedConsumer(
            @Value("${application.messaging.consumer.file-upload-session-closed-event.max-attempts}") final Long maxAttempts,
            @Qualifier("fileUploadSessionClosedEventError") final MessageProducer<FileUploadSessionClosedMessage> errorMessageProducer,
            final ChunkCleanerService chunkCleanerService) {
        return new FileUploadSessionClosedConsumer(
                maxAttempts,
                errorMessageProducer,
                chunkCleanerService);
    }

    @Bean
    Consumer<Message<FileUploadSessionAbortedMessage>> fileUploadSessionAbortedConsumer(
            @Value("${application.messaging.consumer.file-upload-session-aborted-event.max-attempts}") final Long maxAttempts,
            @Qualifier("fileUploadSessionAbortedEventError") final MessageProducer<FileUploadSessionAbortedMessage> errorMessageProducer,
            final ChunkCleanerService chunkCleanerService) {
        return new FileUploadSessionAbortedConsumer(
                maxAttempts,
                errorMessageProducer,
                chunkCleanerService);
    }

}
