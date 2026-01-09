package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file;

import java.util.Objects;
import java.util.Set;

import org.springframework.messaging.Message;

import com.arqivame.storage.application.service.file.FinalizeFileProcessingService;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCompletedMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public class FileUploadSessionCompletedConsumer extends RabbitMQMessageConsumer<FileUploadSessionCompletedMessage> {

    private final FinalizeFileProcessingService finalizeFileProcessingService;

    public FileUploadSessionCompletedConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<FileUploadSessionCompletedMessage> errorMessageProducer,
            final FinalizeFileProcessingService finalizeFileProcessingService) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.finalizeFileProcessingService = Objects.requireNonNull(finalizeFileProcessingService);
    }

    @Override
    public void consume(final Message<FileUploadSessionCompletedMessage> message) {

        final FileUploadSessionCompletedMessage event = message.getPayload();
        final FileUploadSessionCompletedMessage.Data data = event.data();

        finalizeFileProcessingService.finalizeFileProcessing(data.fileId());

    }

}
