package com.arqivame.storage.infrastructure.storage.service.filesystem;

import static com.arqivame.storage.infrastructure.commons.InputStreamUtils.bounded;
import static com.arqivame.storage.infrastructure.commons.InputStreamUtils.digestible;
import static com.arqivame.storage.infrastructure.commons.InputStreamUtils.throttled;

import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Objects;

import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.Checksum.Algorithm;
import com.arqivame.storage.infrastructure.commons.FileSystemUtils;
import com.arqivame.storage.infrastructure.commons.MessageDigestUtils;
import com.arqivame.storage.infrastructure.commons.StringUtils;
import com.arqivame.storage.infrastructure.storage.service.StorageKey;
import com.arqivame.storage.infrastructure.storage.service.StorageService;

public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(final Path rootLocation) {
        this.rootLocation = Objects.requireNonNull(rootLocation);
    }

    @Override
    public void delete(final StorageKey key) {

        final String fullKey = key.getFullKey();

        final Path sessionLocation = rootLocation.resolve(fullKey);

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

        final Path storageLocation = rootLocation.resolve(fullKey);

        final MessageDigest digest = MessageDigestUtils.create(checksumAlgorithm);

        write(
                storageLocation,
                inputStream,
                sizeInBytes,
                bytesPerSecondsWrittenRate,
                digest);

        return new Checksum(StringUtils.toHexString(digest.digest()), checksumAlgorithm);

    }

    @Override
    public InputStream read(
            final StorageKey key,
            final Long offsetInBytes,
            final Long sizeInBytes,
            final Long bytesPerSecondsReadRate) {

        final String fullKey = key.getFullKey();
        final Path storageLocation = rootLocation.resolve(fullKey);

        try {
            final FileChannel channel = FileSystemUtils.openChannel(storageLocation, StandardOpenOption.READ);
            final InputStream inputStream = FileSystemUtils.read(channel, offsetInBytes);
            return throttled(bounded(inputStream, sizeInBytes), bytesPerSecondsReadRate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read input stream", e);
        }

    }

    private static void write(
            final Path fileOutputPath,
            final InputStream inputStream,
            final Long sizeInBytes,
            final Long bytesPerSecondsWrittenRate,
            final MessageDigest digest) {

        try (final InputStream is = digestible(
                throttled(bounded(inputStream, sizeInBytes), bytesPerSecondsWrittenRate),
                digest)) {

            FileSystemUtils.write(fileOutputPath, is, StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            throw new RuntimeException("Failed to write input stream", e);
        }

    }

}
