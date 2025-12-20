package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.file.service.StorageService;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class UploadSession extends Entity<UploadSessionID> {

    private final FileID file;
    private final Instant createdAt;
    private final Duration maxIdleTime;
    private final Long maxBytesPerSecondTransferRatePerChunk;
    private final Integer maxChunksAtSameTime;
    private final Long totalChunks;
    private final Set<Chunk> chunks;

    private UploadSession(
            final UploadSessionID id,
            final FileID file,
            final Instant createdAt,
            final Duration maxIdleTime,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime,
            final Long totalChunks,
            final Set<Chunk> chunks) {
        super(id);
        this.file = Objects.requireNonNull(file);
        this.createdAt = createdAt;
        this.maxIdleTime = maxIdleTime;
        this.maxBytesPerSecondTransferRatePerChunk = maxBytesPerSecondTransferRatePerChunk;
        this.maxChunksAtSameTime = maxChunksAtSameTime;
        this.totalChunks = totalChunks;
        this.chunks = Objects.isNull(chunks) ? new HashSet<>() : new HashSet<>(chunks);
    }

    public static UploadSession create(
            final File file,
            final Long totalChunks,
            final Long chunkSize,
            final Long lastChunkSize,
            final Duration maxIdleTime,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime) {

        if (totalChunks <= 0)
            throw new IllegalArgumentException("Total chunks must be greater than zero");

        if (maxIdleTime.isNegative() || maxIdleTime.isZero())
            throw new IllegalArgumentException("Max idle time must be greater than zero");

        if (maxBytesPerSecondTransferRatePerChunk <= 0)
            throw new IllegalArgumentException("Max bytes per second transfer rate per chunk must be greater than zero");

        if (maxChunksAtSameTime <= 0)
            throw new IllegalArgumentException("Max chunks at same time must be greater than zero");

        final Set<Chunk> chunks = LongStream
                .range(0, totalChunks - 1)
                .mapToObj(index -> Chunk.create(index, chunkSize))
                .collect(Collectors.toCollection(HashSet::new));

        chunks.add(Chunk.create(totalChunks, lastChunkSize));

        return new UploadSession(
                UploadSessionID.unique(),
                file.getId(),
                Instant.now(),
                maxIdleTime,
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime,
                totalChunks,
                chunks);
    }

    public static UploadSession with(
            final UploadSessionID id,
            final FileID file,
            final Instant createdAt,
            final Duration maxIdleTime,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime,
            final Long totalChunks,
            final Set<Chunk> chunks) {
        return new UploadSession(
                id,
                file,
                createdAt,
                maxIdleTime,
                maxBytesPerSecondTransferRatePerChunk,
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
        // throw new RuntimeException("Upload session idle time exceeded");

        chunks.add(chunk);
        return this;
    }

    public UploadSession initiateChunkWriting(final Long chunkIndex, final StorageService writer) {

        final Long writingChunksCount = chunks
                .stream()
                .filter(chunk -> chunk.getStatus().equals(ChunkStatus.WRITING))
                .count();

        if (writingChunksCount >= maxChunksAtSameTime)
            throw new RuntimeException("Maximum number of chunks being written reached: " + maxChunksAtSameTime);

        chunks
                .stream()
                .filter(chunk -> chunk.getIndex().equals(chunkIndex))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chunk not found: " + chunkIndex))
                .assignWriter(writer);

        return this;
    }

    public UploadSession writeChunk(
            final Long chunkIndex,
            final Checksum checksumValue,
            final InputStream inputStream) {

        chunks
                .stream()
                .filter(chunk -> chunk.getIndex().equals(chunkIndex))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chunk not found: " + chunkIndex))
                .write(this, inputStream, checksumValue, maxBytesPerSecondTransferRatePerChunk);

        return this;
    }

    public Boolean isComplete() {
        return this.chunks.size() >= this.totalChunks;
    }

    public FileID getFile() {
        return file;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public Long getMaxBytesPerSecondTransferRatePerChunk() {
        return maxBytesPerSecondTransferRatePerChunk;
    }

    public Integer getMaxChunksAtSameTime() {
        return maxChunksAtSameTime;
    }

    public Long getTotalChunks() {
        return totalChunks;
    }

    public Set<Chunk> getChunks() {
        return Set.copyOf(chunks);
    }

    // private Boolean isIdleTimeExceeded() {
    // final Instant now = Instant.now();

    // final Instant lastActivity = this.chunks
    // .stream()
    // .map(Chunk::getWrittenAt)
    // .max(Instant::compareTo)
    // .orElse(this.createdAt);

    // final Duration idleTime = Duration.between(lastActivity, now);

    // return idleTime.compareTo(maxIdleTime) > 0;
    // }

}
