package com.arqivame.storage.infrastructure.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.stream.Stream;

import com.arqivame.storage.domain.exception.InternalErrorException;

public final class FileSystemUtils {

    private FileSystemUtils() {
    }

    public static Boolean exists(final Path filePath) {
        return Files.exists(filePath);
    }

    public static void write(
            final Path outputLocation,
            final InputStream content,
            final CopyOption... options) {

        if (content == null)
            throw new IllegalArgumentException("Failed to write empty file.");

        final Path destinationFile = outputLocation.normalize().toAbsolutePath();

        try (InputStream inputStream = content) {
            Files.createDirectories(destinationFile.getParent());
            Files.copy(inputStream, destinationFile, options);

        } catch (FileAlreadyExistsException e) {
            throw InternalErrorException.with("File already exists: " + destinationFile.toString(), e);
        } catch (IOException e) {
            throw InternalErrorException.with("Failed to write file.", e);
        }

    }

    public static void delete(final Path filePath) {
        try {

            if (Files.notExists(filePath))
                return;

            try (Stream<Path> walk = Files.walk(filePath)) {
                walk.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
            }

        } catch (IOException e) {
            throw InternalErrorException.with("Failed to delete file: " + filePath.toString(), e);
        }
    }

    public static FileChannel opeChannel(final Path path, final StandardOpenOption... options) {
        try {
            return FileChannel.open(path, options);
        } catch (IOException e) {
            throw InternalErrorException.with("Failed to open file channel: " + path.toString(), e);
        }
    }

    public static void append(
            final Long startPosition,
            final FileChannel targetChannel,
            final FileChannel sourceChannel) {

        try {

            targetChannel.position(startPosition);

            Long inputSize = sourceChannel.size();
            Long trasferredBytes = 0L;
            while (trasferredBytes < inputSize) {
                trasferredBytes += sourceChannel.transferTo(
                        trasferredBytes,
                        inputSize - trasferredBytes,
                        targetChannel);
            }

        } catch (Exception e) {
            throw InternalErrorException.with("Failed to append file channels.", e);
        }

    }

}
