package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file;

import java.util.Objects;
import java.util.Set;

import org.springframework.messaging.Message;

import com.arqivame.storage.infrastructure.file.model.FileUploadSessionAbortedMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;
import com.arqivame.storage.infrastructure.storage.service.ChunkStorageCleaner;

public class FileUploadSessionAbortedConsumer extends RabbitMQMessageConsumer<FileUploadSessionAbortedMessage> {

    private final ChunkStorageCleaner chunkStorageCleaner;

    public FileUploadSessionAbortedConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<FileUploadSessionAbortedMessage> errorMessageProducer,
            final ChunkStorageCleaner chunkStorageCleaner) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.chunkStorageCleaner = Objects.requireNonNull(chunkStorageCleaner);
    }

    @Override
    public void consume(final Message<FileUploadSessionAbortedMessage> message) {

        final FileUploadSessionAbortedMessage event = message.getPayload();
        final FileUploadSessionAbortedMessage.Data data = event.data();

        chunkStorageCleaner.clearChunks(data.fileId());

    }

}
