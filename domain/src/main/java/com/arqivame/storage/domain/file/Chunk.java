package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.file.service.StorageKey;
import com.arqivame.storage.domain.file.service.StorageWriter;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class Chunk extends Entity<ChunkID> {

    private final Long index;
    private final Long size;
    private ChunkStatus status;
    private StorageKey storageKey;

    // TODO precisa? validar
    private Instant writtenAt;

    private Optional<StorageWriter> writer;

    private Chunk(
            final ChunkID id,
            final Long index,
            final Long size,
            final ChunkStatus status,
            final StorageKey storageKey,
            final Instant writtenAt,
            final StorageWriter writer) {
        super(id);
        this.index = index;
        this.size = size;
        this.status = status;
        this.storageKey = storageKey;
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
                null,
                null);
    }

    public static Chunk with(
            final ChunkID id,
            final Long index,
            final Long size,
            final ChunkStatus status,
            final StorageKey storageKey,
            final Instant writtenAt,
            final StorageWriter writer) {
        return new Chunk(
                id,
                index,
                size,
                status,
                storageKey,
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
            throw new IllegalArgumentException("Writer cannot be null");

        this.status = ChunkStatus.READY;
        this.writer = Optional.ofNullable(writer);
        return this;
    }

    public Chunk write(
            final StorageKey key,
            final InputStream inputStream,
            final Checksum checksumValue,
            final Long bytesPerSecondsWrittenRate) {

        if (writer.isEmpty()) {
            this.status = ChunkStatus.FAILED;
            throw new IllegalStateException("Chunk writer is not assigned");
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
            throw new RuntimeException("Checksum mismatch after writing chunk");
        }

        this.status = ChunkStatus.WRITTEN;
        this.storageKey = key;
        this.writtenAt = Instant.now();

        return this;
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

    public Instant getWrittenAt() {
        return writtenAt;
    }

    public Optional<StorageWriter> getWriter() {
        return writer;
    }

}
