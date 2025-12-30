package com.arqivame.storage.application.file.session.process.initiate;

import java.util.Objects;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.exception.NotFoundException;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;

public class DefaultInitiateUploadSessionProcessingUseCase extends InitiateUploadSessionProcessingUseCase {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;

    public DefaultInitiateUploadSessionProcessingUseCase(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public void execute(final InitiateUploadSessionProcessingInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final UploadSessionID sessionId = UploadSessionID.of(input.uploadSessionId());

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        eventDispatcher.notify(fileGateway.update(file.initUploadSessionProcessing(sessionId)));

    }

}
