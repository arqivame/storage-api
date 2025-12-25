package com.arqivame.storage.application.file.session.delete.physical;

import java.util.Objects;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;
import com.arqivame.storage.domain.file.service.StorageDeleter;

public class DefaultPhysicalUploadSessionDeleteUseCase extends PhysicalUploadSessionDeleteUseCase {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;

    private final StorageDeleter storageDeleter;

    public DefaultPhysicalUploadSessionDeleteUseCase(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway,
            final StorageDeleter storageDeleter) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageDeleter = Objects.requireNonNull(storageDeleter);
    }

    @Override
    public void execute(final PhysicalUploadSessionDeleteInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final UploadSessionID uploadSessionId = UploadSessionID.of(input.uploadSessionId());

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: " + input.fileId()));

        eventDispatcher.notify(fileGateway.update(file.physicallyDeleteUploadSession(uploadSessionId, storageDeleter)));

    }

}
