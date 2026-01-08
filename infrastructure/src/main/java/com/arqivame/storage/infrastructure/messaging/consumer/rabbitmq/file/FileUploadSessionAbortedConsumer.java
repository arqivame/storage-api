package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file;

import java.util.Objects;
import java.util.Set;

import org.springframework.messaging.Message;

import com.arqivame.storage.application.service.file.ChunkCleanerService;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionAbortedMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public class FileUploadSessionAbortedConsumer
        extends RabbitMQMessageConsumer<FileUploadSessionAbortedMessage> {

    private final ChunkCleanerService chunkCleanerService;

    public FileUploadSessionAbortedConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<FileUploadSessionAbortedMessage> errorMessageProducer,
            final ChunkCleanerService chunkCleanerService) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.chunkCleanerService = Objects.requireNonNull(chunkCleanerService);
    }

    @Override
    public void consume(final Message<FileUploadSessionAbortedMessage> message) {

        final FileUploadSessionAbortedMessage event = message.getPayload();
        final FileUploadSessionAbortedMessage.Data data = event.data();

        chunkCleanerService.clearChunks(data.fileId());

    }

}
