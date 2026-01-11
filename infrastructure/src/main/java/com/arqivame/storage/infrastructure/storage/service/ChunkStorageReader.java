package com.arqivame.storage.infrastructure.storage.service;

import java.io.InputStream;

import com.arqivame.storage.application.port.ChunkReader;
import com.arqivame.storage.domain.file.FileID;

public class ChunkStorageReader implements ChunkReader {

    private final StorageReader storageReader;

    public ChunkStorageReader(final StorageReader storageReader) {
        this.storageReader = storageReader;
    }

    @Override
    public InputStream readChunk(
            final FileID id,
            final Long offset,
            final Long sizeInBytes,
            final Long bytesPerSecondsWrittenRate) {

        final StorageKey storageKey = StorageKey.create("files", id.getStringValue(), "data");
        return storageReader.read(storageKey, offset, sizeInBytes, bytesPerSecondsWrittenRate);

    }

}
