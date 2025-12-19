package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class Chunk extends Entity<ChunkID> {

    private final Checksum checksum;
    private final Integer index;
    private final Instant uploadedAt;
    private final Optional<InputStream> writableStream;

    private Chunk(
            final ChunkID id,
            final Checksum checksum,
            final Integer index,
            final Instant uploadedAt,
            final InputStream writableStream) {
        super(id);
        this.checksum = checksum;
        this.index = index;
        this.uploadedAt = uploadedAt;
        this.writableStream = Optional.ofNullable(writableStream);
    }

    public static Chunk create(final Checksum checksum, final Integer index, final InputStream writableStream) {
        return new Chunk(ChunkID.unique(), checksum, index, Instant.now(), writableStream);
    }

    public static Chunk from(
            final ChunkID id,
            final Checksum checksum,
            final Integer index,
            final Instant uploadedAt,
            final InputStream writableStream) {
        return new Chunk(id, checksum, index, uploadedAt, writableStream);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public Integer getIndex() {
        return index;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    // TODO find a better way to handle streams inside domain
    // without exposing them directly
    public Optional<InputStream> getWritableStream() {
        return writableStream;
    }

}
