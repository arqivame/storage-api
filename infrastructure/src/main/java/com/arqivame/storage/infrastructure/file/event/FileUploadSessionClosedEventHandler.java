package com.arqivame.storage.infrastructure.file.event;

import org.springframework.stereotype.Component;

import com.arqivame.storage.domain.event.EventHandler;
import com.arqivame.storage.domain.file.event.FileUploadSessionClosedEvent;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionClosedMessage;
import com.arqivame.storage.infrastructure.file.presenter.FilePresenter;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

@Component
public class FileUploadSessionClosedEventHandler extends EventHandler<FileUploadSessionClosedEvent> {

    private final MessageProducer<FileUploadSessionClosedMessage> messageProducer;

    public FileUploadSessionClosedEventHandler(
            final MessageProducer<FileUploadSessionClosedMessage> messageProducer) {
        super(FileUploadSessionClosedEvent.eventKey());
        this.messageProducer = messageProducer;
    }

    @Override
    public void handle(final FileUploadSessionClosedEvent event) {
        messageProducer.produce(FilePresenter.present(event));
    }

}
