package com.arqivame.storage.domain.file.service;

import com.arqivame.storage.domain.exception.InvalidArgumentException;
import com.arqivame.storage.domain.exception.DomainException.Error;

public final class ChunkCalculatorService {

    private ChunkCalculatorService() {
    }

    public static ChunkCalculationResult calculate(final Long maxAllowedChunkSize, final Long fileSize) {

        if (maxAllowedChunkSize <= 0)
            throw InvalidArgumentException.with(Error.with("Max allowed chunk size must be greater than zero."));

        if (fileSize <= 0)
            throw InvalidArgumentException.with(Error.with("File size must be greater than zero."));

        final long fullChunks = fileSize / maxAllowedChunkSize;
        final long hasPartialChunk = fileSize % maxAllowedChunkSize != 0 ? 1 : 0;

        final long totalChunks = fullChunks + hasPartialChunk;
        final long lastChunkSize = hasPartialChunk == 1 ? fileSize % maxAllowedChunkSize : maxAllowedChunkSize;

        return new ChunkCalculationResult(totalChunks, maxAllowedChunkSize, lastChunkSize);

    }

    public record ChunkCalculationResult(Long totalChunks, Long chunkSize, Long lastChunkSize) {
    }

}
