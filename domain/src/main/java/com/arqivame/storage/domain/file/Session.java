package com.arqivame.storage.domain.file;

import java.time.Instant;

import com.arqivame.storage.domain.ValueObject;
import com.arqivame.storage.domain.validation.ValidationError;
import com.arqivame.storage.domain.validation.ValidationHandler;

public record Session(
        Instant createdAt,
        Long maxBytesPerSecondTransferRatePerChunk,
        Integer maxChunksAtSameTime,
        Integer totalChunks,
        Long chunkSize,
        Long lastChunkSize) implements ValueObject {

    public static Session create(
            final Integer totalChunks,
            final Long chunkSize,
            final Long lastChunkSize,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime) {

        return new Session(
                Instant.now(),
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime,
                totalChunks,
                chunkSize,
                lastChunkSize);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (totalChunks <= 0)
            handler.append(ValidationError.with("Total chunks must be greater than zero"));

        if (maxBytesPerSecondTransferRatePerChunk <= 0)
            handler.append(
                    ValidationError.with("Max bytes per second transfer rate per chunk must be greater than zero"));

        if (maxChunksAtSameTime <= 0)
            handler.append(ValidationError.with("Max chunks at same time must be greater than zero"));

    }

}
