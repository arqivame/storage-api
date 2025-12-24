package com.arqivame.storage.application.file.session.delete.mark;

import java.util.Objects;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;

public class DefaultMarkUploadSessionForDeletionUseCase extends MarkUploadSessionForDeletionUseCase {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;

    public DefaultMarkUploadSessionForDeletionUseCase(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public void execute(final MarkUploadSessionForDeletionInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final UploadSessionID uploadSessionId = UploadSessionID.of(input.uploadSessionId());

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: " + input.fileId()));

        eventDispatcher.notify(fileGateway.update(file.markUploadSessionForDeletion(uploadSessionId)));

    }

}
