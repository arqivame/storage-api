package com.arqivame.storage.infrastructure.commons;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.arqivame.storage.domain.exception.InternalErrorException;

public final class FileSystemUtils {

    private FileSystemUtils() {
    }

    public static void write(
            final Path location,
            final String fileName,
            final InputStream content,
            final CopyOption... options) {

        if (content == null)
            throw new IllegalArgumentException("Failed to write empty file.");

        final Path destinationFile = location.resolve(fileName).normalize().toAbsolutePath();

        try (InputStream inputStream = content) {
            Files.createDirectories(destinationFile.getParent());
            Files.copy(inputStream, destinationFile, options);

        } catch (FileAlreadyExistsException e) {
            throw new IllegalArgumentException("File already exists: " + destinationFile.toString(), e);
        } catch (IOException e) {
            throw InternalErrorException.with("Failed to write file.", e);
        }

    }

    public static void delete(final Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw InternalErrorException.with("Failed to delete file: " + filePath.toString(), e);
        }
    }

    public static void append(final Path targetFile, final SequentialIterator<Path> sourceFiles) {

        try (final FileChannel outputChannel = FileChannel.open(
                targetFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {

            while (sourceFiles.hasNext()) {
                try (final FileChannel inputChannel = FileChannel.open(sourceFiles.next(), StandardOpenOption.READ)) {

                    final Long startPosition = outputChannel.position();// TODO ver se isso ta certo
                    final Long endPosition = append(startPosition, outputChannel, inputChannel);
                    outputChannel.position(endPosition);

                }
            }

        } catch (Exception e) {
            throw InternalErrorException.with("Failed to append file into " + targetFile.toString(), e);
        }

    }

    private static Long append(
            final Long startPosition,
            final FileChannel targetChannel,
            final FileChannel sourceChannel) throws Exception {

        targetChannel.position(startPosition);

        Long inputSize = sourceChannel.size();
        Long trasferredBytes = 0L;
        while (trasferredBytes < inputSize) {
            trasferredBytes += sourceChannel.transferTo(trasferredBytes, inputSize - trasferredBytes, targetChannel);
        }

        final Long endPosition = targetChannel.position() + inputSize;
        targetChannel.position(endPosition);

        return endPosition;
    }

}
