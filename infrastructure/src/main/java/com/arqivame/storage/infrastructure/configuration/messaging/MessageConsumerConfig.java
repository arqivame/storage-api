package com.arqivame.storage.infrastructure.configuration.messaging;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.arqivame.storage.application.service.file.FinalizerFileProcessingService;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionAbortedMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionClosedMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCompletedMessage;
import com.arqivame.storage.infrastructure.file.service.FileAssembler;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file.FileUploadSessionAbortedConsumer;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file.FileUploadSessionClosedConsumer;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file.FileUploadSessionCompletedConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;
import com.arqivame.storage.infrastructure.storage.service.ChunkStorageCleaner;

@Configuration
public class MessageConsumerConfig {

    private final FileGateway fileGateway;
    private final FileAssembler fileAssembler;
    private final FinalizerFileProcessingService finalizerFileProcessingService;
    private final ChunkStorageCleaner chunkStorageCleaner;

    public MessageConsumerConfig(
            final FileGateway fileGateway,
            final FileAssembler fileAssembler,
            final FinalizerFileProcessingService finalizerFileProcessingService,
            final ChunkStorageCleaner chunkStorageCleaner) {
        this.fileGateway = fileGateway;
        this.fileAssembler = fileAssembler;
        this.finalizerFileProcessingService = finalizerFileProcessingService;
        this.chunkStorageCleaner = chunkStorageCleaner;
    }

    @Bean
    Consumer<Message<FileUploadSessionCompletedMessage>> fileUploadSessionCompletedConsumer(
            @Value("${application.messaging.consumer.file-upload-session-completed-event.max-attempts}") final Long maxAttempts,
            @Qualifier("fileUploadSessionCompletedEventError") final MessageProducer<FileUploadSessionCompletedMessage> errorMessageProducer,
            final FinalizerFileProcessingService finalizeFileProcessingService) {

        return new FileUploadSessionCompletedConsumer(
                maxAttempts,
                errorMessageProducer,
                fileGateway,
                fileAssembler,
                finalizeFileProcessingService);
    }

    @Bean
    Consumer<Message<FileUploadSessionClosedMessage>> fileUploadSessionClosedConsumer(
            @Value("${application.messaging.consumer.file-upload-session-closed-event.max-attempts}") final Long maxAttempts,
            @Qualifier("fileUploadSessionClosedEventError") final MessageProducer<FileUploadSessionClosedMessage> errorMessageProducer) {
        return new FileUploadSessionClosedConsumer(
                maxAttempts,
                errorMessageProducer,
                chunkStorageCleaner);
    }

    @Bean
    Consumer<Message<FileUploadSessionAbortedMessage>> fileUploadSessionAbortedConsumer(
            @Value("${application.messaging.consumer.file-upload-session-aborted-event.max-attempts}") final Long maxAttempts,
            @Qualifier("fileUploadSessionAbortedEventError") final MessageProducer<FileUploadSessionAbortedMessage> errorMessageProducer) {
        return new FileUploadSessionAbortedConsumer(
                maxAttempts,
                errorMessageProducer,
                chunkStorageCleaner);
    }

}
