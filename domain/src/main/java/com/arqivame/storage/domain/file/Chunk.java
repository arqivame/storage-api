package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;

public class Chunk {

    private final Integer index;
    private final Instant uploadedAt;

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

    public Integer getIndex() {
        return index;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public Optional<InputStream> getWritableStream() {
        return writableStream;
    }

}
