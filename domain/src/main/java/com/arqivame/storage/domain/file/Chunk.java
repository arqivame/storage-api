package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;

public class Chunk {

    private final Integer index;
    private final Instant uploadedAt;
    private Boolean writePending;

    private final Optional<InputStream> writableStream;

    private Chunk(
            final Integer index,
            final Instant uploadedAt,
            final InputStream writableStream) {
        this.index = index;
        this.uploadedAt = uploadedAt;
        this.writableStream = Optional.ofNullable(writableStream);
    }

    public static Chunk create(final Integer index, final InputStream writableStream) {
        return new Chunk(index, Instant.now(), writableStream);
    }

    public static Chunk from(
            final Integer index,
            final Instant uploadedAt,
            final InputStream writableStream) {
        return new Chunk(index, uploadedAt, writableStream);
    }

    public Chunk writeContent(final ChunkStreamWriter writer) {

        writableStream
                .ifPresentOrElse(
                        writer::write,
                        () -> {
                            throw new IllegalStateException("No writable stream available for this chunk.");
                        });

        this.writePending = false;
        return this;
    }

    public Integer getIndex() {
        return index;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public Boolean isWritePending() {
        return writePending;
    }

    // TODO find a better way to handle streams inside domain
    // without exposing them directly
    public Optional<InputStream> getWritableStream() {
        return writableStream;
    }

}
