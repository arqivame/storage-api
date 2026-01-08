package com.arqivame.storage.application.service.file;

import java.util.Objects;
import java.util.UUID;

import com.arqivame.storage.domain.exception.NotFoundException;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.service.StorageDeleter;

public class ChunkCleanerService {

    private final FileGateway fileGateway;

    private final StorageDeleter storageDeleter;

    public ChunkCleanerService(
            final FileGateway fileGateway,
            final StorageDeleter storageDeleter) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageDeleter = Objects.requireNonNull(storageDeleter);
    }

    public void clearChunks(final UUID fileId) {

        final File file = fileGateway
                .findById(FileID.of(fileId))
                .orElseThrow(() -> NotFoundException.create(File.class, FileID.of(fileId)));

        file.getUploadedChunks()
                .forEach(chunk -> storageDeleter.delete(
                        file.getStorageKey().subKey("upload", "chunks", chunk.index().toString())));

    }

}
