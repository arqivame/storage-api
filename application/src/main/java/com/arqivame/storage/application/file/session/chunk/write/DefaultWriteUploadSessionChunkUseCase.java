package com.arqivame.storage.application.file.session.chunk.write;

import java.io.InputStream;
import java.util.Objects;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;
import com.arqivame.storage.domain.file.service.StorageWriter;
import com.arqivame.storage.domain.validation.ValidationHandler;
import com.arqivame.storage.domain.validation.handler.Notification;

public class DefaultWriteUploadSessionChunkUseCase extends WriteUploadSessionChunkUseCase {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;

    private final StorageWriter storageService;

    public DefaultWriteUploadSessionChunkUseCase(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway,
            final StorageWriter storageService) {
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

        final ValidationHandler validation = Notification.create();

        final File file = findFileById(fileId);

        prepareForChunkWriting(
                validation,
                file,
                sessionId,
                chunkIndex);

        writeChunk(
                validation,
                file,
                sessionId,
                chunkIndex,
                checksumValue,
                chunkData);

    }

    private File findFileById(final FileID fileId) {
        return fileGateway
                .findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: " + fileId.getValue()));
    }

    private void prepareForChunkWriting(
            final ValidationHandler validation,
            final File file,
            final UploadSessionID sessionId,
            final Long chunkIndex) {

        validation.validate(() -> file.initiateChunkWriting(sessionId, chunkIndex, storageService));
        eventDispatcher.notify(fileGateway.update(file));
        if (validation.hasError())
            throw new RuntimeException("Cannot initiate chunk writing: " + validation.getErrors().toString());

    }

    private void writeChunk(
            final ValidationHandler validation,
            final File file,
            final UploadSessionID sessionId,
            final Long chunkIndex,
            final Checksum checksumValue,
            final InputStream chunkData) {

        validation.validate(() -> file.writeChunk(sessionId, chunkIndex, checksumValue, chunkData));
        eventDispatcher.notify(fileGateway.update(file));
        if (validation.hasError())
            throw new RuntimeException("Cannot write chunk data: " + validation.getErrors().toString());

    }

}
