package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file;

import java.util.Objects;
import java.util.Set;

import org.springframework.messaging.Message;

import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionClosedMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;
import com.arqivame.storage.infrastructure.storage.service.ChunkStorageCleaner;

public class FileUploadSessionClosedConsumer extends RabbitMQMessageConsumer<FileUploadSessionClosedMessage> {

    private final ChunkStorageCleaner chunkStorageCleaner;

    public FileUploadSessionClosedConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<FileUploadSessionClosedMessage> errorMessageProducer,
            final ChunkStorageCleaner chunkStorageCleaner) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.chunkStorageCleaner = Objects.requireNonNull(chunkStorageCleaner);
    }

    @Override
    public void consume(final Message<FileUploadSessionClosedMessage> message) {

        final FileUploadSessionClosedMessage event = message.getPayload();
        final FileUploadSessionClosedMessage.Data data = event.data();

        final FileID fileId = FileID.of(data.fileId());

        chunkStorageCleaner.clearChunks(fileId.getValue());

    }

}
