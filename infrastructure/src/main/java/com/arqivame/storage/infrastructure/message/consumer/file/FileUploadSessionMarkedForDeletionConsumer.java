package com.arqivame.storage.infrastructure.message.consumer.file;

import java.util.Objects;

import com.arqivame.storage.application.file.session.delete.physical.PhysicalUploadSessionDelete;
import com.arqivame.storage.application.file.session.delete.physical.PhysicalUploadSessionDeleteInput;
import com.arqivame.storage.domain.file.event.FileUploadSessionMarkedForDeletionEvent;
import com.arqivame.storage.infrastructure.message.consumer.MessageConsumer;

public class FileUploadSessionMarkedForDeletionConsumer
        implements MessageConsumer<FileUploadSessionMarkedForDeletionEvent.Data> {

    private final PhysicalUploadSessionDelete physicalUploadSessionDelete;

    public FileUploadSessionMarkedForDeletionConsumer(final PhysicalUploadSessionDelete physicalUploadSessionDelete) {
        this.physicalUploadSessionDelete = Objects.requireNonNull(physicalUploadSessionDelete);
    }

    @Override
    public void consume(final FileUploadSessionMarkedForDeletionEvent.Data message) {

        physicalUploadSessionDelete
                .execute(new PhysicalUploadSessionDeleteInput(message.fileId(), message.sessionId()));

    }

}
