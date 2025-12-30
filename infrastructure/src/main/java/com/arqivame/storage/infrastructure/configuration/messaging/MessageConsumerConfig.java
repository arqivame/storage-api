package com.arqivame.storage.infrastructure.configuration.messaging;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.arqivame.storage.application.file.session.chunk.merge.MergeUploadSessionChunksUseCase;
import com.arqivame.storage.application.file.session.delete.mark.MarkUploadSessionForDeletionUseCase;
import com.arqivame.storage.application.file.session.delete.physical.PhysicalUploadSessionDeleteUseCase;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCanceledMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionMarkedForDeletionMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionProcessingInitiatedMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file.FileUploadSessionCanceledConsumer;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file.FileUploadSessionMarkedForDeletionConsumer;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file.FileUploadSessionProcessingInitiatedConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

@Configuration
public class MessageConsumerConfig {

    @Bean
    Consumer<Message<FileUploadSessionCanceledMessage>> fileUploadSessionCanceledConsumer(
            @Value("${application.messaging.consumer.file-upload-session-canceled-event.max-attempts}") final Long maxAttempts,
            @Qualifier("fileUploadSessionCanceledEventError") final MessageProducer<FileUploadSessionCanceledMessage> errorMessageProducer,
            final MarkUploadSessionForDeletionUseCase markUploadSessionForDeletionUseCase) {
        return new FileUploadSessionCanceledConsumer(
                maxAttempts,
                errorMessageProducer,
                markUploadSessionForDeletionUseCase);
    }

    @Bean
    Consumer<Message<FileUploadSessionMarkedForDeletionMessage>> fileUploadSessionMarkedForDeletionConsumer(
            @Value("${application.messaging.consumer.file-upload-session-marked-for-deletion-event.max-attempts}") final Long maxAttempts,
            @Qualifier("fileUploadSessionMarkedForDeletionEventError") final MessageProducer<FileUploadSessionMarkedForDeletionMessage> errorMessageProducer,
            final PhysicalUploadSessionDeleteUseCase physicalUploadSessionDeleteUseCase) {
        return new FileUploadSessionMarkedForDeletionConsumer(
                maxAttempts,
                errorMessageProducer,
                physicalUploadSessionDeleteUseCase);
    }

    @Bean
    Consumer<Message<FileUploadSessionProcessingInitiatedMessage>> fileUploadSessionProcessingInitiatedConsumer(
            @Value("${application.messaging.consumer.file-upload-session-processing-initiated-event.max-attempts}") final Long maxAttempts,
            @Qualifier("fileUploadSessionProcessingInitiatedEventError") final MessageProducer<FileUploadSessionProcessingInitiatedMessage> errorMessageProducer,
            final MergeUploadSessionChunksUseCase mergeUploadSessionChunksUseCase) {
        return new FileUploadSessionProcessingInitiatedConsumer(
                maxAttempts,
                errorMessageProducer,
                mergeUploadSessionChunksUseCase);
    }

}
