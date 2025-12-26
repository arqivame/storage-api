package com.arqivame.storage.infrastructure.file.event;

import org.springframework.stereotype.Component;

import com.arqivame.storage.domain.event.EventHandler;
import com.arqivame.storage.domain.file.event.FileUploadSessionCanceledEvent;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

@Component
public class FileUploadSessionCanceledEventHandler extends EventHandler<FileUploadSessionCanceledEvent> {

    private final MessageProducer<FileUploadSessionCanceledEvent> messageProducer;

    public FileUploadSessionCanceledEventHandler(
            final MessageProducer<FileUploadSessionCanceledEvent> messageProducer) {
        super(FileUploadSessionCanceledEvent.eventKey());
        this.messageProducer = messageProducer;
    }

    @Override
    public void handle(final FileUploadSessionCanceledEvent event) {
        messageProducer.produce(event);
    }

}
