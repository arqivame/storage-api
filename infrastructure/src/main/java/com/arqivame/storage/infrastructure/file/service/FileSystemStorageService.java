package com.arqivame.storage.infrastructure.file.service;

import static com.arqivame.storage.infrastructure.commons.InputStreamUtils.bounded;
import static com.arqivame.storage.infrastructure.commons.InputStreamUtils.digestible;
import static com.arqivame.storage.infrastructure.commons.InputStreamUtils.throttled;

import java.io.InputStream;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Objects;

import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.Checksum.Algorithm;
import com.arqivame.storage.domain.file.service.StorageKey;
import com.arqivame.storage.domain.file.service.StorageService;
import com.arqivame.storage.infrastructure.commons.FileSystemUtils;
import com.arqivame.storage.infrastructure.commons.MessageDigestUtils;
import com.arqivame.storage.infrastructure.commons.StringUtils;

public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(final Path rootLocation) {
        this.rootLocation = Objects.requireNonNull(rootLocation);
    }

    @Override
    public void delete(final StorageKey key) {

        final String fullKey = key.getFullKey();
        final String lastSegment = key.lastSegment();

        final Path sessionLocation = rootLocation.resolve(fullKey).resolve(lastSegment);

        FileSystemUtils.delete(sessionLocation);

    }

    @Override
    public Checksum write(
            final StorageKey key,
            final InputStream inputStream,
            final Long sizeInBytes,
            final Long bytesPerSecondsWrittenRate,
            final Algorithm checksumAlgorithm) {

        final String fullKey = key.getFullKey();
        final String lastSegment = key.lastSegment();

        final Path sessionLocation = rootLocation.resolve(fullKey);

        final MessageDigest digest = MessageDigestUtils.create(checksumAlgorithm);

        write(
                sessionLocation,
                lastSegment,
                inputStream,
                sizeInBytes,
                bytesPerSecondsWrittenRate,
                digest);

        return new Checksum(StringUtils.toHexString(digest.digest()), checksumAlgorithm);

    }

    private static void write(
            final Path sessionLocation,
            final String lastSegment,
            final InputStream inputStream,
            final Long sizeInBytes,
            final Long bytesPerSecondsWrittenRate,
            final MessageDigest digest) {

        try (final InputStream is = digestible(
                throttled(bounded(inputStream, sizeInBytes), bytesPerSecondsWrittenRate),
                digest)) {

            FileSystemUtils.write(
                    sessionLocation,
                    lastSegment,
                    is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to write input stream", e);
        }

    }

}
