package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file;

import java.util.Objects;
import java.util.Set;

import org.springframework.messaging.Message;

import com.arqivame.storage.application.file.session.delete.mark.MarkUploadSessionForDeletionInput;
import com.arqivame.storage.application.file.session.delete.mark.MarkUploadSessionForDeletionUseCase;
import com.arqivame.storage.domain.file.event.FileUploadSessionCanceledEvent;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public class FileUploadSessionCanceledConsumer extends RabbitMQMessageConsumer<FileUploadSessionCanceledEvent> {

    private final MarkUploadSessionForDeletionUseCase markUploadSessionForDeletion;

    public FileUploadSessionCanceledConsumer(
            final Integer maxRetryAttempts,
            final MessageProducer<Message<FileUploadSessionCanceledEvent>> errorMessageProducer,
            final MarkUploadSessionForDeletionUseCase markUploadSessionForDeletion) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.markUploadSessionForDeletion = Objects.requireNonNull(markUploadSessionForDeletion);
    }

    @Override
    public void consume(final Message<FileUploadSessionCanceledEvent> message) {

        final FileUploadSessionCanceledEvent event = message.getPayload();
        final FileUploadSessionCanceledEvent.Data data = event.getData();

        final MarkUploadSessionForDeletionInput input = new MarkUploadSessionForDeletionInput(
                data.fileId(),
                data.sessionId());

        markUploadSessionForDeletion.execute(input);

    }

}
