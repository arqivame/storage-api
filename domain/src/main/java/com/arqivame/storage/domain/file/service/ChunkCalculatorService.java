package com.arqivame.storage.domain.file.service;

public class ChunkCalculatorService {

    private final Long maxAllowedChunkSize;

    public ChunkCalculatorService(final Long maxAllowedChunkSize) {

        if (maxAllowedChunkSize <= 0)
            throw new IllegalArgumentException("Max allowed chunk size must be greater than zero.");

        this.maxAllowedChunkSize = maxAllowedChunkSize;
    }

    public ChunkCalculationResult calculate(final Long fileSize) {

        if (fileSize <= 0)
            throw new IllegalArgumentException("File size must be greater than zero.");

        final long fullChunks = fileSize / maxAllowedChunkSize;
        final long hasPartialChunk = fileSize % maxAllowedChunkSize != 0 ? 1 : 0;

        final long totalChunks = fullChunks + hasPartialChunk;
        final long lastChunkSize = hasPartialChunk == 1 ? fileSize % maxAllowedChunkSize : maxAllowedChunkSize;

        return new ChunkCalculationResult(totalChunks, maxAllowedChunkSize, lastChunkSize);

    }

    public record ChunkCalculationResult(Long totalChunks, Long chunkSize, Long lastChunkSize) {
    }

}
