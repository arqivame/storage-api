package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file;

import java.util.Objects;
import java.util.Set;

import org.springframework.messaging.Message;

import com.arqivame.storage.application.file.session.delete.physical.PhysicalUploadSessionDeleteInput;
import com.arqivame.storage.application.file.session.delete.physical.PhysicalUploadSessionDeleteUseCase;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionMarkedForDeletionMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public class FileUploadSessionMarkedForDeletionConsumer
        extends RabbitMQMessageConsumer<FileUploadSessionMarkedForDeletionMessage> {

    private final PhysicalUploadSessionDeleteUseCase physicalUploadSessionDeleteUseCase;

    public FileUploadSessionMarkedForDeletionConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<FileUploadSessionMarkedForDeletionMessage> errorMessageProducer,
            final PhysicalUploadSessionDeleteUseCase physicalUploadSessionDeleteUseCase) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.physicalUploadSessionDeleteUseCase = Objects.requireNonNull(physicalUploadSessionDeleteUseCase);
    }

    @Override
    public void consume(final Message<FileUploadSessionMarkedForDeletionMessage> message) {

        final FileUploadSessionMarkedForDeletionMessage event = message.getPayload();
        final FileUploadSessionMarkedForDeletionMessage.Data data = event.data();

        final PhysicalUploadSessionDeleteInput input = new PhysicalUploadSessionDeleteInput(
                data.fileId(),
                data.sessionId());

        physicalUploadSessionDeleteUseCase.execute(input);

    }

}
