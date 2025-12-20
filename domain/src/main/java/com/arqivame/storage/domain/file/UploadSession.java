package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.file.service.InputStreamWriter;
import com.arqivame.storage.domain.file.service.UploadSessionChunksWriter;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class UploadSession extends Entity<UploadSessionID> {

    private final Instant createdAt;
    private final Duration maxIdleTime;
    private final Long maxBitsPerSecondTransferRatePerChunk;
    private final Integer maxChunksAtSameTime;
    private final Integer totalChunks;
    private final Set<Chunk> chunks;

    private UploadSession(
            final UploadSessionID id,
            final Instant createdAt,
            final Duration maxIdleTime,
            final Long maxBitsPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime,
            final Integer totalChunks,
            final Set<Chunk> chunks) {
        super(id);
        this.createdAt = createdAt;
        this.maxIdleTime = maxIdleTime;
        this.maxBitsPerSecondTransferRatePerChunk = maxBitsPerSecondTransferRatePerChunk;
        this.maxChunksAtSameTime = maxChunksAtSameTime;
        this.totalChunks = totalChunks;
        this.chunks = Objects.isNull(chunks) ? new HashSet<>() : new HashSet<>(chunks);
    }

    public static UploadSession create(
            final Integer totalChunks,
            final Duration maxIdleTime,
            final Long maxBitsPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime) {

        if (totalChunks <= 0)
            throw new IllegalArgumentException("Total chunks must be greater than zero");

        if (maxIdleTime.isNegative() || maxIdleTime.isZero())
            throw new IllegalArgumentException("Max idle time must be greater than zero");

        if (maxBitsPerSecondTransferRatePerChunk <= 0)
            throw new IllegalArgumentException("Max bits per second transfer rate per chunk must be greater than zero");

        if (maxChunksAtSameTime <= 0)
            throw new IllegalArgumentException("Max chunks at same time must be greater than zero");

        final Set<Chunk> chunks = IntStream
                .range(0, totalChunks)
                .mapToObj(Chunk::create)
                .collect(Collectors.toSet());

        return new UploadSession(
                UploadSessionID.unique(),
                Instant.now(),
                maxIdleTime,
                maxBitsPerSecondTransferRatePerChunk,
                maxChunksAtSameTime,
                totalChunks,
                chunks);
    }

    public static UploadSession with(
            final UploadSessionID id,
            final Instant createdAt,
            final Duration maxIdleTime,
            final Long maxBitsPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime,
            final Integer totalChunks,
            final Set<Chunk> chunks) {
        return new UploadSession(
                id,
                createdAt,
                maxIdleTime,
                maxBitsPerSecondTransferRatePerChunk,
                maxChunksAtSameTime,
                totalChunks,
                chunks);
    }

    @Override
    public void validate(ValidationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    public UploadSession addChunk(final Chunk chunk) {

        // if (isIdleTimeExceeded())
        //     throw new RuntimeException("Upload session idle time exceeded");

        chunks.add(chunk);
        return this;
    }

    public UploadSession markChunkAsWriting(final Integer chunkIndex) {

        final Long writingChunksCount = chunks.stream()
                .filter(chunk -> chunk.status().equals(ChunkStatus.WRITING))
                .count();

        if (writingChunksCount >= maxChunksAtSameTime)
            throw new RuntimeException("Maximum number of chunks being written reached: " + maxChunksAtSameTime);

        chunks.stream()
                .filter(chunk -> chunk.index().equals(chunkIndex))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chunk not found: " + chunkIndex))
                .markAsWriting();

        return this;
    }

    public UploadSession writeChunk(
            final Integer chunkIndex,
            final InputStream inputStream,
            final InputStreamWriter writer) {

        chunks.stream()
                .filter(chunk -> chunk.index().equals(chunkIndex))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chunk not found: " + chunkIndex))
                .assignWriter(writer)
                .write(inputStream, maxBitsPerSecondTransferRatePerChunk);

        return this;
    }

    public Boolean isComplete() {
        return this.chunks.size() >= this.totalChunks;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public Integer getTotalChunks() {
        return totalChunks;
    }

    public Set<Chunk> getChunks() {
        return Set.copyOf(chunks);
    }

    // private Boolean isIdleTimeExceeded() {
    //     final Instant now = Instant.now();

    //     final Instant lastActivity = this.chunks
    //             .stream()
    //             .map(Chunk::getWrittenAt)
    //             .max(Instant::compareTo)
    //             .orElse(this.createdAt);

    //     final Duration idleTime = Duration.between(lastActivity, now);

    //     return idleTime.compareTo(maxIdleTime) > 0;
    // }

}
