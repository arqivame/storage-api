package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file;

import java.util.Objects;
import java.util.Set;

import org.springframework.messaging.Message;

import com.arqivame.storage.application.file.session.delete.mark.MarkUploadSessionForDeletionInput;
import com.arqivame.storage.application.file.session.delete.mark.MarkUploadSessionForDeletionUseCase;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCanceledMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public class FileUploadSessionCanceledConsumer
        extends RabbitMQMessageConsumer<FileUploadSessionCanceledMessage> {

    private final MarkUploadSessionForDeletionUseCase markUploadSessionForDeletion;

    public FileUploadSessionCanceledConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<Message<FileUploadSessionCanceledMessage>> errorMessageProducer,
            final MarkUploadSessionForDeletionUseCase markUploadSessionForDeletion) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.markUploadSessionForDeletion = Objects.requireNonNull(markUploadSessionForDeletion);
    }

    @Override
    public void consume(final Message<FileUploadSessionCanceledMessage> message) {

        final FileUploadSessionCanceledMessage event = message.getPayload();
        final FileUploadSessionCanceledMessage.Data data = event.data();

        final MarkUploadSessionForDeletionInput input = new MarkUploadSessionForDeletionInput(
                data.fileId(),
                data.sessionId());

        markUploadSessionForDeletion.execute(input);

    }

}
