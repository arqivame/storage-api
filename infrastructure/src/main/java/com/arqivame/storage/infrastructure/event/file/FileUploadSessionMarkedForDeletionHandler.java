package com.arqivame.storage.infrastructure.event.file;

import java.util.Objects;

import com.arqivame.storage.application.file.session.delete.physical.PhysicalUploadSessionDeleteUseCase;
import com.arqivame.storage.application.file.session.delete.physical.PhysicalUploadSessionDeleteInput;
import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventHandler;
import com.arqivame.storage.domain.file.event.FileUploadSessionMarkedForDeletionEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionMarkedForDeletionEvent.Data;

public abstract class FileUploadSessionMarkedForDeletionHandler
        extends EventHandler<FileUploadSessionMarkedForDeletionEvent.Data> {

    private final PhysicalUploadSessionDeleteUseCase physicalUploadSessionDelete;

    protected FileUploadSessionMarkedForDeletionHandler(
            final PhysicalUploadSessionDeleteUseCase physicalUploadSessionDelete) {
        super(FileUploadSessionMarkedForDeletionEvent.eventKey());
        this.physicalUploadSessionDelete = Objects.requireNonNull(physicalUploadSessionDelete);
    }

    @Override
    public void handle(final Event<Data> event) {

        final PhysicalUploadSessionDeleteInput input = new PhysicalUploadSessionDeleteInput(
                event.getData().fileId(),
                event.getData().sessionId());

        physicalUploadSessionDelete.execute(input);

    }

}
