package com.arqivame.storage.application.file.session.chunk.write;

import java.io.InputStream;
import java.util.Objects;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;
import com.arqivame.storage.domain.file.service.StorageService;
import com.arqivame.storage.domain.validation.ValidationHandler;
import com.arqivame.storage.domain.validation.handler.Notification;

public class DefaultWriteUploadSessionChunkUseCase extends WriteUploadSessionChunkUseCase {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;

    private final StorageService storageService;

    public DefaultWriteUploadSessionChunkUseCase(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway,
            final StorageService storageService) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageService = Objects.requireNonNull(storageService);
    }

    @Override
    public void execute(final WriteUploadSessionChunkInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final UploadSessionID sessionId = UploadSessionID.of(input.uploadSessionId());
        final Long chunkIndex = input.chunkIndex();
        final Checksum checksumValue = Checksum.from(input.checksumValue(), input.checksumAlgorithm());
        final InputStream chunkData = input.chunkData();

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: " + input.fileId()));

        final ValidationHandler validation = Notification.create();

        validation.validate(() -> file.initiateChunkWriting(sessionId, chunkIndex, storageService));
        eventDispatcher.notify(fileGateway.update(file));
        if (validation.hasError())
            throw new RuntimeException("Cannot initiate chunk writing: " + validation.getErrors().toString());

        validation.validate(() -> file.writeChunk(sessionId, chunkIndex, checksumValue, chunkData));
        eventDispatcher.notify(fileGateway.update(file));
        if (validation.hasError())
            throw new RuntimeException("Cannot write chunk data: " + validation.getErrors().toString());

    }

}
