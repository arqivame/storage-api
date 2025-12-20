package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.file.service.InputStreamWriter;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class Chunk extends Entity<ChunkID> {

    private final Long index;
    private ChunkStatus status;

    private Instant writtedAt;

    private Checksum checksum;
    private Boolean isPersisted;

    private Optional<InputStreamWriter> writer;

    // private Optional<InputStream> writableStream;

    private Chunk(
            final ChunkID id,
            final Long index,
            final ChunkStatus status,
            final Boolean isPersisted,
            final Checksum checksum,
            final Instant writtedAt,
            final InputStream writableStream) {
        super(id);
        this.index = index;
        this.status = status;
        this.isPersisted = isPersisted;
        this.checksum = checksum;
        this.writtedAt = writtedAt;
        // this.writableStream = Optional.ofNullable(writableStream);
    }

    public static Chunk create(final Long index) {
        return new Chunk(
                ChunkID.unique(),
                index,
                ChunkStatus.PENDING,
                false,
                null,
                null,
                null);
    }

    // public static Chunk create(final Checksum checksum, final Integer index,
    // final InputStream writableStream) {
    // return new Chunk(ChunkID.unique(), checksum, index, Instant.now(),
    // writableStream);
    // }

    public static Chunk from(
            final ChunkID id,
            final Long index,
            final ChunkStatus status,
            final Boolean isPersisted,
            final Checksum checksum,
            final Instant writtedAt,
            final InputStream writableStream) {
        return new Chunk(
                id,
                index,
                status,
                isPersisted,
                checksum,
                writtedAt,
                writableStream);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    // public Chunk markAsWritten(final Checksum checksum) {
    public Chunk markAsWritten() {

        // if (this.checksum == null)
        // throw new IllegalStateException("Cannot mark chunk as written without a
        // checksum");

        // if (this.checksum != null && !this.checksum.equals(checksum))
        // throw new IllegalStateException("Cannot mark chunk as written with an invalid
        // checksum");

        this.status = ChunkStatus.WRITTEN;
        this.isPersisted = true;

        this.writtedAt = Instant.now();
        return this;
    }

    public Chunk markAsWriting() {
        this.status = ChunkStatus.WRITING;
        return this;
    }

    public Chunk assignWriter(final InputStreamWriter writer) {

        if (writer == null)
            throw new IllegalArgumentException("Writer cannot be null");

        this.writer = Optional.ofNullable(writer);
        return this;
    }

    public Chunk write(final InputStream inputStream, final Long bitsPerSecondsWrittenRate) {

        if (writer.isEmpty())
            throw new RuntimeException("No writer available for this chunk");

        final var w = writer.get();
        w.write(inputStream, bitsPerSecondsWrittenRate);
        this.writtedAt = Instant.now();

        return this;
    }

    public Long index() {
        return index;
    }

    public ChunkStatus status() {
        return status;
    }

}
