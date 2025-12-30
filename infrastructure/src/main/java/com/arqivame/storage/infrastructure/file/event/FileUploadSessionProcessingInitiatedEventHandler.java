package com.arqivame.storage.infrastructure.file.event;

import org.springframework.stereotype.Component;

import com.arqivame.storage.domain.event.EventHandler;
import com.arqivame.storage.domain.file.event.FileUploadSessionProcessingInitiatedEvent;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionProcessingInitiatedMessage;
import com.arqivame.storage.infrastructure.file.presenter.FilePresenter;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

@Component
public class FileUploadSessionProcessingInitiatedEventHandler
        extends EventHandler<FileUploadSessionProcessingInitiatedEvent> {

    private final MessageProducer<FileUploadSessionProcessingInitiatedMessage> messageProducer;

    public FileUploadSessionProcessingInitiatedEventHandler(
            final MessageProducer<FileUploadSessionProcessingInitiatedMessage> messageProducer) {
        super(FileUploadSessionProcessingInitiatedEvent.eventKey());
        this.messageProducer = messageProducer;
    }

    @Override
    public void handle(final FileUploadSessionProcessingInitiatedEvent event) {
        messageProducer.produce(FilePresenter.present(event));
    }

}
