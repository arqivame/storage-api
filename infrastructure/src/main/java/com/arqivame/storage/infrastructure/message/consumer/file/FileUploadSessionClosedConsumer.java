package com.arqivame.storage.infrastructure.message.consumer.file;

import com.arqivame.storage.domain.file.event.FileUploadSessionClosedEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionClosedEvent.Data;
import com.arqivame.storage.infrastructure.message.consumer.MessageConsumer;

public class FileUploadSessionClosedConsumer implements MessageConsumer<FileUploadSessionClosedEvent.Data> {

    // private final

    @Override
    public void consume(final Data message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consume'");
    }

}
