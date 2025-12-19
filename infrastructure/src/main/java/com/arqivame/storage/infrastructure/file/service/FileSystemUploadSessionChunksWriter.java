package com.arqivame.storage.infrastructure.file.service;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.UploadSessionID;
import com.arqivame.storage.domain.file.service.UploadSessionChunksWriter;
import com.arqivame.storage.infrastructure.commons.FileSystemUtils;

public class FileSystemUploadSessionChunksWriter implements UploadSessionChunksWriter {

    private final Path rootLocation;

    public FileSystemUploadSessionChunksWriter(final Path rootLocation) {
        this.rootLocation = Objects.requireNonNull(rootLocation);
    }

    @Override
    public void write(final UploadSessionID uploadSessionId, final Set<Chunk> chunks) {

        final Path sessionLocation = rootLocation.resolve(uploadSessionId.getStringValue());

        chunks
                .stream()
                .filter(chunk -> chunk.getWritableStream().isPresent())
                .forEach(chunk -> FileSystemUtils.write(
                        sessionLocation,
                        chunk.getIndex().toString(),
                        chunk.getWritableStream().orElse(null)));

    }

}
