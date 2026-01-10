package com.arqivame.storage.infrastructure.storage.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class ChunkStorageCleaner {

    private final StorageDeleter storageDeleter;

    public ChunkStorageCleaner(final StorageDeleter storageDeleter) {
        this.storageDeleter = storageDeleter;
    }

    public void clearChunks(final UUID fileId) {

        storageDeleter.delete(StorageKey.create("files", fileId.toString()).subKey("upload", "chunks"));

    }

}
