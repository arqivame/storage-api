package com.arqivame.storage.infrastructure.file.event;

import org.springframework.stereotype.Component;

import com.arqivame.storage.domain.event.EventHandler;
import com.arqivame.storage.domain.file.event.FileUploadSessionAbortedEvent;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionAbortedMessage;
import com.arqivame.storage.infrastructure.file.presenter.FilePresenter;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

@Component
public class FileUploadSessionAbortedEventHandler extends EventHandler<FileUploadSessionAbortedEvent> {

    private final MessageProducer<FileUploadSessionAbortedMessage> messageProducer;

    public FileUploadSessionAbortedEventHandler(
            final MessageProducer<FileUploadSessionAbortedMessage> messageProducer) {
        super(FileUploadSessionAbortedEvent.eventKey());
        this.messageProducer = messageProducer;
    }

    @Override
    public void handle(final FileUploadSessionAbortedEvent event) {
        messageProducer.produce(FilePresenter.present(event));
    }

}
