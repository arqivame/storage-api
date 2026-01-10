package com.arqivame.storage.infrastructure.storage.service;

import java.io.InputStream;
import java.util.Objects;

import com.arqivame.storage.application.port.ChunkWriter;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.Checksum.Algorithm;
import com.arqivame.storage.domain.file.FileID;

public class ChunkStorageWriter implements ChunkWriter {

    private final StorageWriter storageWriter;

    public ChunkStorageWriter(final StorageWriter storageWriter) {
        this.storageWriter = Objects.requireNonNull(storageWriter);
    }

    @Override
    public Checksum writeChunk(
            final FileID key,
            final Long chunkIndex,
            final InputStream inputStream,
            final Long sizeInBytes,
            final Long bytesPerSecondsWrittenRate,
            final Algorithm checksumAlgorithm) {

        final StorageKey chunkStorageKey = StorageKey.create("files", key.getStringValue())
                .subKey(
                        "upload",
                        "chunks",
                        chunkIndex.toString());

        return storageWriter.write(
                chunkStorageKey,
                inputStream,
                sizeInBytes,
                bytesPerSecondsWrittenRate,
                checksumAlgorithm);

    }

}
