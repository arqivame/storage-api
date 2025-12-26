package com.arqivame.storage.infrastructure.configuration.messaging;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.arqivame.storage.application.file.session.delete.mark.MarkUploadSessionForDeletionUseCase;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCanceledMessage;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file.FileUploadSessionCanceledConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

@Configuration
public class MessageConsumerConfig {

    @Bean
    Consumer<Message<FileUploadSessionCanceledMessage>> fileUploadSessionCanceledConsumer(
            @Qualifier("fileUploadSessionCanceledEventError") final MessageProducer<FileUploadSessionCanceledMessage> errorMessageProducer,
            final MarkUploadSessionForDeletionUseCase markUploadSessionForDeletionUseCase) {
        return new FileUploadSessionCanceledConsumer(2L, errorMessageProducer, markUploadSessionForDeletionUseCase);
    }

}
