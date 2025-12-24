package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.file.service.StorageDeleter;
import com.arqivame.storage.domain.file.service.StorageKey;
import com.arqivame.storage.domain.file.service.StorageWriter;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class UploadSession extends Entity<UploadSessionID> {

    private UploadSessionStatus status;

    private final FileID file;
    private final Instant createdAt;
    private final Duration maxIdleTime;
    private final Long maxBytesPerSecondTransferRatePerChunk;
    private final Integer maxChunksAtSameTime;
    private final Long totalChunks;
    private final Long chunkSize;
    private final Long lastChunkSize;
    private final Set<Chunk> chunks;
    private Boolean waitingForDeletion;

    private Long version;

    private UploadSession(
            final UploadSessionID id,
            final UploadSessionStatus status,
            final FileID file,
            final Instant createdAt,
            final Duration maxIdleTime,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime,
            final Long totalChunks,
            final Long chunkSize,
            final Long lastChunkSize,
            final Set<Chunk> chunks,
            final Boolean waitingForDeletion,
            final Long version) {
        super(id);
        this.status = status;
        this.file = Objects.requireNonNull(file);
        this.createdAt = createdAt;
        this.maxIdleTime = maxIdleTime;
        this.maxBytesPerSecondTransferRatePerChunk = maxBytesPerSecondTransferRatePerChunk;
        this.maxChunksAtSameTime = maxChunksAtSameTime;
        this.totalChunks = totalChunks;
        this.chunkSize = chunkSize;
        this.lastChunkSize = lastChunkSize;
        this.chunks = Objects.isNull(chunks) ? new HashSet<>() : new HashSet<>(chunks);
        this.waitingForDeletion = waitingForDeletion;

        this.version = version;
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
            throw new IllegalArgumentException(
                    "Max bytes per second transfer rate per chunk must be greater than zero");

        if (maxChunksAtSameTime <= 0)
            throw new IllegalArgumentException("Max chunks at same time must be greater than zero");

        return new UploadSession(
                UploadSessionID.unique(),
                UploadSessionStatus.ACTIVE,
                file.getId(),
                Instant.now(),
                maxIdleTime,
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime,
                totalChunks,
                chunkSize,
                lastChunkSize,
                Set.of(),
                false,
                null);
    }

    public static UploadSession with(
            final UploadSessionID id,
            final UploadSessionStatus status,
            final FileID file,
            final Instant createdAt,
            final Duration maxIdleTime,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime,
            final Long totalChunks,
            final Long chunkSize,
            final Long lastChunkSize,
            final Set<Chunk> chunks,
            final Boolean waitingForDeletion,
            final Long version) {
        return new UploadSession(
                id,
                status,
                file,
                createdAt,
                maxIdleTime,
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime,
                totalChunks,
                chunkSize,
                lastChunkSize,
                chunks,
                waitingForDeletion,
                version);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    public UploadSession initiateChunkWriting(final Long chunkIndex, final StorageWriter writer) {

        if (UploadSessionStatus.CANCELED.equals(this.status))
            throw new RuntimeException("Cannot write chunk to a canceled upload session: " + this.getId().getValue());

        final Long writingChunksCount = chunks
                .stream()
                .filter(chunk -> chunk.getStatus().equals(ChunkStatus.READY))
                .count();

        if (writingChunksCount >= maxChunksAtSameTime)
            throw new RuntimeException("Maximum number of chunks being written reached: " + maxChunksAtSameTime);

        chunks
                .stream()
                .filter(chunk -> chunk.getIndex().equals(chunkIndex))
                .findFirst()
                .orElseGet(() -> createChunk(chunkIndex))
                .assignWriter(writer);

        return this;
    }

    public UploadSession writeChunk(
            final Long chunkIndex,
            final Checksum checksumValue,
            final InputStream inputStream) {

        if (UploadSessionStatus.CANCELED.equals(this.status))
            throw new RuntimeException("Cannot write chunk to a canceled upload session: " + this.getId().getValue());

        final StorageKey key = StorageKey.from(getFile(), getId(), chunkIndex);

        chunks
                .stream()
                .filter(chunk -> chunk.getIndex().equals(chunkIndex))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chunk not found: " + chunkIndex))
                .write(key, inputStream, checksumValue, maxBytesPerSecondTransferRatePerChunk);

        return this;
    }

    public UploadSession cancel() {
        this.status = UploadSessionStatus.CANCELED;
        return this;
    }

    public void markForDeletion() {

        // TODO validar essa regra de negocio
        if (UploadSessionStatus.ACTIVE.equals(this.status))
            throw new IllegalStateException(
                    "Cannot mark an active upload session for deletion: " + this.getId().getValue());

        if (UploadSessionStatus.PROCESSING.equals(this.status))
            throw new IllegalStateException(
                    "Cannot mark a processing upload session for deletion: " + this.getId().getValue());

        chunks.forEach(Chunk::markForDeletion);

        waitingForDeletion = true;
    }

    public void physicallyDeleteChunks(final StorageDeleter storageDeleter) {

        chunks
                .stream()
                .filter(Chunk::getWaitingForDeletion)
                .forEach(chunk -> chunk.physicallyDelete(storageDeleter));

        this.status = UploadSessionStatus.DELETED;

    }

    private Chunk createChunk(final Long index) {

        if (index < 0 || index >= totalChunks)
            throw new IllegalArgumentException("Chunk index out of bounds: " + index);

        final Long size = (index == totalChunks - 1) ? lastChunkSize : chunkSize;

        final Chunk chunk = Chunk.create(index, size);
        this.chunks.add(chunk);

        return chunk;
    }

    public Boolean isComplete() {
        return this.chunks.size() >= this.totalChunks;
    }

    public UploadSessionStatus getStatus() {
        return status;
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

    public Long getChunkSize() {
        return chunkSize;
    }

    public Long getLastChunkSize() {
        return lastChunkSize;
    }

    public Set<Chunk> getChunks() {
        return Set.copyOf(chunks);
    }

    public Boolean getWaitingForDeletion() {
        return waitingForDeletion;
    }

    public Long getVersion() {
        return version;
    }

}
