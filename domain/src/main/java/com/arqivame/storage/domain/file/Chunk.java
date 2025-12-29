package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.exception.ChunkIntegrityViolationException;
import com.arqivame.storage.domain.exception.InvalidArgumentException;
import com.arqivame.storage.domain.exception.InvalidStateException;
import com.arqivame.storage.domain.exception.DomainException.Error;
import com.arqivame.storage.domain.file.service.StorageDeleter;
import com.arqivame.storage.domain.file.service.StorageKey;
import com.arqivame.storage.domain.file.service.StorageWriter;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class Chunk extends Entity<ChunkID> {

    private final Long index;
    private final Long size;
    private ChunkStatus status;
    private StorageKey storageKey;
    private Boolean waitingForDeletion;

    private Instant writtenAt;

    private Optional<StorageWriter> writer;

    private Chunk(
            final ChunkID id,
            final Long index,
            final Long size,
            final ChunkStatus status,
            final StorageKey storageKey,
            final Boolean waitingForDeletion,
            final Instant writtenAt,
            final StorageWriter writer) {
        super(id);
        this.index = index;
        this.size = size;
        this.status = status;
        this.storageKey = storageKey;
        this.waitingForDeletion = waitingForDeletion;
        this.writtenAt = writtenAt;
        this.writer = Optional.ofNullable(writer);
    }

    public static Chunk create(final Long index, final Long size) {
        return new Chunk(
                ChunkID.unique(),
                index,
                size,
                ChunkStatus.PENDING,
                null,
                false,
                null,
                null);
    }

    public static Chunk with(
            final ChunkID id,
            final Long index,
            final Long size,
            final ChunkStatus status,
            final StorageKey storageKey,
            final Boolean waitingForDeletion,
            final Instant writtenAt,
            final StorageWriter writer) {
        return new Chunk(
                id,
                index,
                size,
                status,
                storageKey,
                waitingForDeletion,
                writtenAt,
                writer);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    public Chunk assignWriter(final StorageWriter writer) {

        if (Objects.isNull(writer))
            throw InvalidArgumentException.with(Error.with("Writer cannot be null"));

        if (waitingForDeletion)
            throw InvalidStateException.with(
                    Chunk.class,
                    Error.with("Cannot assign writer to a chunk marked for deletion: " + this.getId().getValue()));

        this.status = ChunkStatus.READY;
        this.writer = Optional.ofNullable(writer);
        return this;
    }

    public Chunk write(
            final StorageKey key,
            final InputStream inputStream,
            final Checksum checksumValue,
            final Long bytesPerSecondsWrittenRate) {

        if (waitingForDeletion)
            throw InvalidStateException.with(
                    Chunk.class,
                    Error.with("Cannot write a chunk marked for deletion: " + this.getId().getValue()));

        if (writer.isEmpty()) {
            this.status = ChunkStatus.FAILED;
            throw InvalidStateException.with(
                    Chunk.class,
                    Error.with("Chunk writer is not assigned: " + this.getId().getValue()));
        }

        final Checksum streamChecksumValue = writer
                .get()
                .write(
                        key,
                        inputStream,
                        size,
                        bytesPerSecondsWrittenRate,
                        checksumValue.algorithm());

        if (!streamChecksumValue.equals(checksumValue)) {
            status = ChunkStatus.FAILED;
            throw ChunkIntegrityViolationException.create();
        }

        this.status = ChunkStatus.WRITTEN;
        this.storageKey = key;
        this.writtenAt = Instant.now();

        return this;
    }

    public void markForDeletion() {

        waitingForDeletion = true;

        if (ChunkStatus.PENDING.equals(this.status) || ChunkStatus.READY.equals(this.status))
            this.status = ChunkStatus.ABANDONED;

    }

    public void physicallyDelete(final StorageDeleter storageDeleter) {

        if (Objects.isNull(storageKey))
            return;

        storageDeleter.delete(storageKey);
        this.status = ChunkStatus.DELETED;
    }

    public Long getIndex() {
        return index;
    }

    public Long getSize() {
        return size;
    }

    public ChunkStatus getStatus() {
        return status;
    }

    public Optional<StorageKey> getStorageKey() {
        return Optional.ofNullable(storageKey);
    }

    public Boolean getWaitingForDeletion() {
        return waitingForDeletion;
    }

    public Instant getWrittenAt() {
        return writtenAt;
    }

    public Optional<StorageWriter> getWriter() {
        return writer;
    }

}
