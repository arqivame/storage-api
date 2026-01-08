package com.arqivame.storage.application.usecase.file.session.upload.abort;

import java.util.Objects;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.exception.NotFoundException;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;

public class DefaultAbortUploadSessionUseCase extends AbortUploadSessionUseCase {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;

    public DefaultAbortUploadSessionUseCase(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public void execute(final AbortUploadSessionInput input) {

        final FileID fileId = FileID.of(input.fileId());

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        eventDispatcher.notify(fileGateway.update(file.abortUploadSession()));

    }

}
