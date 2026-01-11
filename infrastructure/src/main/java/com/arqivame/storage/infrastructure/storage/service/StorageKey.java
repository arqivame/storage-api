package com.arqivame.storage.infrastructure.storage.service;

import java.util.Arrays;
import java.util.Objects;

import com.arqivame.storage.domain.exception.DomainException.Error;
import com.arqivame.storage.domain.exception.InvalidArgumentException;

public final class StorageKey {

    private static final String SEGMENT_SEPARATOR = "/";

    private final String[] segments;

    private StorageKey(final String... segments) {

        if (Objects.isNull(segments) || segments.length == 0)
            throw InvalidArgumentException.with(Error.with("Segments cannot be null or empty"));

        this.segments = segments;
    }

    public static StorageKey create(final String... subSegments) {
        return new StorageKey(subSegments);
    }

    public StorageKey subKey(final String... subSegments) {

        final String[] combined = Arrays.copyOf(
                segments,
                segments.length + subSegments.length);

        System.arraycopy(
                subSegments,
                0,
                combined,
                segments.length,
                subSegments.length);

        return new StorageKey(combined);
    }

    public static StorageKey of(final String fullKey) {
        if (Objects.isNull(fullKey) || fullKey.isBlank())
            throw InvalidArgumentException.with(Error.with("Full key cannot be null or blank"));

        final String[] segments = fullKey.split(SEGMENT_SEPARATOR);
        return new StorageKey(segments);
    }

    // TODO talvez adicionar "storage(type)(info)" para ter N tipos de storage
    // ex: local, s3, gcs, azure, etc

    public String getFullKey() {
        return String.join(SEGMENT_SEPARATOR, segments);
    }

    public String lastSegment() {
        return segments[segments.length - 1];
    }

}
