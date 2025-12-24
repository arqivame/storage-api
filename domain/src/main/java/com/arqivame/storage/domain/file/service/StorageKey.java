package com.arqivame.storage.domain.file.service;

import java.util.Objects;

import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;

public final class StorageKey {

    private static final String SEGMENT_SEPARATOR = "/";

    private final String[] segments;

    private StorageKey(final String... segments) {

        if (Objects.isNull(segments) || segments.length == 0)
            throw new IllegalArgumentException("Segments cannot be null or empty");

        this.segments = segments;
    }

    public static StorageKey from(
            final FileID file,
            final UploadSessionID session,
            final Long chunkIndex) {

        return new StorageKey(
                new String[] {
                        "files",
                        file.getStringValue(),
                        "uploads",
                        session.getStringValue(),
                        "chunks",
                        "index",
                        chunkIndex.toString()
                });
    }

    public static StorageKey of(final String fullKey) {
        if (Objects.isNull(fullKey) || fullKey.isBlank())
            throw new IllegalArgumentException("Full key cannot be null or blank");

        final String[] segments = fullKey.split(SEGMENT_SEPARATOR);
        return new StorageKey(segments);
    }

    public String getFullKey() {
        return String.join(SEGMENT_SEPARATOR, segments);
    }

    public String lastSegment() {
        return segments[segments.length - 1];
    }

}
