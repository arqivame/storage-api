package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file;

import java.util.Objects;
import java.util.Set;

import org.springframework.messaging.Message;

import com.arqivame.storage.application.file.session.chunk.merge.MergeUploadSessionChunksInput;
import com.arqivame.storage.application.file.session.chunk.merge.MergeUploadSessionChunksUseCase;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionProcessingInitiatedMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public class FileUploadSessionProcessingInitiatedConsumer
        extends RabbitMQMessageConsumer<FileUploadSessionProcessingInitiatedMessage> {

    private final MergeUploadSessionChunksUseCase mergeUploadSessionChunksUseCase;

    public FileUploadSessionProcessingInitiatedConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<FileUploadSessionProcessingInitiatedMessage> errorMessageProducer,
            final MergeUploadSessionChunksUseCase mergeUploadSessionChunksUseCase) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.mergeUploadSessionChunksUseCase = Objects.requireNonNull(mergeUploadSessionChunksUseCase);
    }

    @Override
    public void consume(final Message<FileUploadSessionProcessingInitiatedMessage> message) {

        final FileUploadSessionProcessingInitiatedMessage event = message.getPayload();
        final FileUploadSessionProcessingInitiatedMessage.Data data = event.data();

        final MergeUploadSessionChunksInput input = new MergeUploadSessionChunksInput(data.fileId(), data.sessionId());

        mergeUploadSessionChunksUseCase.execute(input);

    }

}
