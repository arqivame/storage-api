package com.arqivame.storage.infrastructure.file.service;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import org.apache.commons.io.input.ThrottledInputStream;

import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.Checksum.Algorithm;
import com.arqivame.storage.domain.file.service.StorageService;
import com.arqivame.storage.infrastructure.commons.FileSystemUtils;

public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(final Path rootLocation) {
        this.rootLocation = Objects.requireNonNull(rootLocation);
    }

    @Override
    public Checksum write(
            final StorageKey key,
            final InputStream inputStream,
            final Long bytesPerSecondsWrittenRate,
            final Algorithm checksumAlgorithm) {

        final String fullKey = key.getFullKey();
        final String lastSegment = key.lastSegment();

        final Path sessionLocation = rootLocation.resolve(fullKey);

        writeThrottleInputStream(
                sessionLocation,
                lastSegment,
                inputStream,
                bytesPerSecondsWrittenRate);

        return new Checksum("123-abc", checksumAlgorithm);

    }

    private static void writeThrottleInputStream(
            final Path sessionLocation,
            final String lastSegment,
            final InputStream inputStream,
            final Long bytesPerSecondsWrittenRate) {

        try (final InputStream is = ThrottledInputStream
                .builder()
                .setInputStream(inputStream)
                .setMaxBytes(bytesPerSecondsWrittenRate, ChronoUnit.SECONDS)
                .get()) {

            FileSystemUtils.write(
                    sessionLocation,
                    lastSegment,
                    is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to throttle input stream", e);
        }

    }

}
