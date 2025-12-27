package com.arqivame.storage.infrastructure.file.event;

import org.springframework.stereotype.Component;

import com.arqivame.storage.domain.event.EventHandler;
import com.arqivame.storage.domain.file.event.FileUploadSessionMarkedForDeletionEvent;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionMarkedForDeletionMessage;
import com.arqivame.storage.infrastructure.file.presenter.FilePresenter;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

@Component
public class FileUploadSessionMarkedForDeletionEventHandler
        extends EventHandler<FileUploadSessionMarkedForDeletionEvent> {

    private final MessageProducer<FileUploadSessionMarkedForDeletionMessage> messageProducer;

    public FileUploadSessionMarkedForDeletionEventHandler(
            final MessageProducer<FileUploadSessionMarkedForDeletionMessage> messageProducer) {
        super(FileUploadSessionMarkedForDeletionEvent.eventKey());
        this.messageProducer = messageProducer;
    }

    @Override
    public void handle(final FileUploadSessionMarkedForDeletionEvent event) {
        messageProducer.produce(FilePresenter.present(event));
    }

}
