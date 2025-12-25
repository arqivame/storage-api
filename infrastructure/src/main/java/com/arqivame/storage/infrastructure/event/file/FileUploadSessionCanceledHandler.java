package com.arqivame.storage.infrastructure.event.file;

import java.util.Objects;

import com.arqivame.storage.application.file.session.delete.mark.MarkUploadSessionForDeletionInput;
import com.arqivame.storage.application.file.session.delete.mark.MarkUploadSessionForDeletionUseCase;
import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventHandler;
import com.arqivame.storage.domain.file.event.FileUploadSessionCanceledEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionCanceledEvent.Data;

public abstract class FileUploadSessionCanceledHandler extends EventHandler<FileUploadSessionCanceledEvent.Data> {

    private final MarkUploadSessionForDeletionUseCase markUploadSessionForDeletion;

    protected FileUploadSessionCanceledHandler(final MarkUploadSessionForDeletionUseCase markUploadSessionForDeletion) {
        super(FileUploadSessionCanceledEvent.eventKey());
        this.markUploadSessionForDeletion = Objects.requireNonNull(markUploadSessionForDeletion);
    }

    @Override
    public void handle(final Event<Data> event) {

        final MarkUploadSessionForDeletionInput input = new MarkUploadSessionForDeletionInput(
                event.getData().fileId(),
                event.getData().sessionId());

        markUploadSessionForDeletion.execute(input);

    }

}
