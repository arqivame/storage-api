package com.arqivame.storage.infrastructure.file.event;

import com.arqivame.storage.domain.event.EventHandler;
import com.arqivame.storage.domain.file.event.FileUploadSessionCompletedEvent;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCompletedMessage;
import com.arqivame.storage.infrastructure.file.presenter.FilePresenter;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

public class FileUploadSessionCompletedEventHandler extends EventHandler<FileUploadSessionCompletedEvent> {

    private final MessageProducer<FileUploadSessionCompletedMessage> messageProducer;

    public FileUploadSessionCompletedEventHandler(
            final MessageProducer<FileUploadSessionCompletedMessage> messageProducer) {
        super(FileUploadSessionCompletedEvent.eventKey());
        this.messageProducer = messageProducer;
    }

    @Override
    public void handle(final FileUploadSessionCompletedEvent event) {
        messageProducer.produce(FilePresenter.present(event));
    }

}
