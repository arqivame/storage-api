package com.arqivame.storage.infrastructure.file.event;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventHandler;
import com.arqivame.storage.domain.file.event.FileUploadSessionCanceledEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionCanceledEvent.Data;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;

@Component
public class FileUploadSessionCanceledEventHandler extends EventHandler<FileUploadSessionCanceledEvent.Data> {

    private final MessageProducer<Event<Data>> messageProducer;

    public FileUploadSessionCanceledEventHandler(
            final MessageProducer<Event<Data>> messageProducer) {
        super(FileUploadSessionCanceledEvent.eventKey());
        this.messageProducer = Objects.requireNonNull(messageProducer);
    }

    @Override
    public void handle(final Event<Data> event) {
        messageProducer.produce(event);
    }

}
