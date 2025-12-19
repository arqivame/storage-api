package com.arqivame.storage.domain.file.service;

import com.arqivame.storage.domain.file.File;

public final class UploadSessionProvisioner {

    private UploadSessionProvisioner() {
    }

    public static File provide(final File file, final Long maxAllowedChunkSize) {

        final var aham = file.getSize() % maxAllowedChunkSize != 0
                ? createUploadSessionWithExtraChunk(file, maxAllowedChunkSize)
                : createUploadSessionWithoutExtraChunk(file, maxAllowedChunkSize);

        return file;

    }

    private static java.util.Map<String, Long> createUploadSessionWithExtraChunk(
            final File file,
            final Long maxAllowedChunkSize) {
        final long size = file.getSize();
        final long fullChunks = size / maxAllowedChunkSize;
        final long lastChunkSize = size % maxAllowedChunkSize;
        final long totalChunks = fullChunks + 1;
        return java.util.Map.of(
                "totalChunks", totalChunks,
                "lastChunkSize", lastChunkSize);
    }

    private static java.util.Map<String, Long> createUploadSessionWithoutExtraChunk(
            final File file,
            final Long maxAllowedChunkSize) {
        final long size = file.getSize();
        final long totalChunks = size / maxAllowedChunkSize;
        return java.util.Map.of(
                "totalChunks", totalChunks,
                "lastChunkSize", maxAllowedChunkSize);
    }

}
