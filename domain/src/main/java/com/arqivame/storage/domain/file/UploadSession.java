package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.exception.DomainException.Error;
import com.arqivame.storage.domain.exception.InvalidArgumentException;
import com.arqivame.storage.domain.exception.InvalidStateException;
import com.arqivame.storage.domain.exception.MaxConcurrentChunkWritesReachedException;
import com.arqivame.storage.domain.exception.NotFoundException;
import com.arqivame.storage.domain.file.service.StorageDeleter;
import com.arqivame.storage.domain.file.service.StorageKey;
import com.arqivame.storage.domain.file.service.StorageWriter;
import com.arqivame.storage.domain.validation.ValidationError;
import com.arqivame.storage.domain.validation.ValidationHandler;
import com.arqivame.storage.domain.validation.handler.Notification;

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

        selfValidate();

    }

    public static UploadSession create(
            final File file,
            final Long totalChunks,
            final Long chunkSize,
            final Long lastChunkSize,
            final Duration maxIdleTime,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime) {

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

        if (totalChunks <= 0)
            handler.append(ValidationError.with("Total chunks must be greater than zero"));

        if (maxIdleTime.isNegative() || maxIdleTime.isZero())
            handler.append(ValidationError.with("Max idle time must be greater than zero"));

        if (maxBytesPerSecondTransferRatePerChunk <= 0)
            handler.append(
                    ValidationError.with("Max bytes per second transfer rate per chunk must be greater than zero"));

        if (maxChunksAtSameTime <= 0)
            handler.append(ValidationError.with("Max chunks at same time must be greater than zero"));

    }

    public UploadSession initiateChunkWriting(final Long chunkIndex, final StorageWriter writer) {

        if (UploadSessionStatus.CANCELED.equals(this.status))
            throw InvalidStateException
                    .with(
                            UploadSession.class,
                            Error.with("Cannot write chunk to a canceled upload session."));

        final Long writingChunksCount = chunks
                .stream()
                .filter(chunk -> chunk.getStatus().equals(ChunkStatus.READY))
                .count();

        if (writingChunksCount >= maxChunksAtSameTime)
            throw MaxConcurrentChunkWritesReachedException.create(maxChunksAtSameTime);

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
            throw InvalidStateException
                    .with(
                            UploadSession.class,
                            Error.with("Cannot write chunk to a canceled upload session."));

        final StorageKey key = StorageKey.from(getFile(), getId(), chunkIndex);

        chunks
                .stream()
                .filter(chunk -> chunk.getIndex().equals(chunkIndex))
                .findFirst()
                .orElseThrow(() -> NotFoundException.create(Chunk.class))
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
            throw InvalidStateException
                    .with(
                            UploadSession.class,
                            Error.with("Cannot mark an active upload session for deletion."));

        if (UploadSessionStatus.PROCESSING.equals(this.status))
            throw InvalidStateException
                    .with(
                            UploadSession.class,
                            Error.with("Cannot mark a processing upload session for deletion."));

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
            throw InvalidArgumentException.with(Error.with("Chunk index out of bounds: " + index));

        final Long size = (index == totalChunks - 1) ? lastChunkSize : chunkSize;

        final Chunk chunk = Chunk.create(index, size);
        this.chunks.add(chunk);

        return chunk;
    }

    private void selfValidate() {
        final Notification notification = Notification.create();
        validate(notification);
        if (notification.hasErrors())
            throw InvalidStateException.with(UploadSession.class, notification.getDomainErrors());
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
