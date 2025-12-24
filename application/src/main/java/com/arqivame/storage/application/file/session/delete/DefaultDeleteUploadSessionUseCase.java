package com.arqivame.storage.application.file.session.delete;

import java.util.Objects;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;
import com.arqivame.storage.domain.file.service.StorageService;

public class DefaultDeleteUploadSessionUseCase extends DeleteUploadSessionUseCase {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;

    private final StorageService storageService;

    public DefaultDeleteUploadSessionUseCase(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway,
            final StorageService storageService) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageService = Objects.requireNonNull(storageService);
    }

    @Override
    public void execute(final DeleteUploadSessionUseCaseInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final UploadSessionID uploadSessionId = UploadSessionID.of(input.uploadSessionId());

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: " + input.fileId()));

        file.deleteUploadSession(uploadSessionId);

    }

}
