package com.arqivame.storage.application.service.file;

import java.util.Objects;
import java.util.UUID;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;

//TODO melhorar nome da classe
public class FinalizerFileProcessingService {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;

    public FinalizerFileProcessingService(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    public void finalize(final UUID fileId, final Boolean isSuccess) {

        final File file = fileGateway
                .findById(FileID.of(fileId))
                .orElseThrow(); // TODO exception

        if (isSuccess) {
            file.markAsAvailable();
        } else {
            // file.markAsFailed(); //TODO implementar
        }

        eventDispatcher.notify(fileGateway.update(file));

    }

}
