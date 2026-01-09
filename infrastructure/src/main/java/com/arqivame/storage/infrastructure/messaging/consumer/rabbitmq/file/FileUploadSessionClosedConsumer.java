package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file;

import java.util.Objects;
import java.util.Set;

import org.springframework.messaging.Message;

import com.arqivame.storage.application.service.file.ChunkCleanerService;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionClosedMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public class FileUploadSessionClosedConsumer extends RabbitMQMessageConsumer<FileUploadSessionClosedMessage> {

    private final ChunkCleanerService chunkCleanerService;

    public FileUploadSessionClosedConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<FileUploadSessionClosedMessage> errorMessageProducer,
            final ChunkCleanerService chunkCleanerService) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.chunkCleanerService = Objects.requireNonNull(chunkCleanerService);
    }

    @Override
    public void consume(final Message<FileUploadSessionClosedMessage> message) {

        final FileUploadSessionClosedMessage event = message.getPayload();
        final FileUploadSessionClosedMessage.Data data = event.data();

        chunkCleanerService.clearChunks(data.fileId());

    }

}
