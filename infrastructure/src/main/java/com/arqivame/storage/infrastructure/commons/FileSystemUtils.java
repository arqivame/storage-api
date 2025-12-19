package com.arqivame.storage.infrastructure.commons;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        final Path destinationFile = location.resolve(Paths.get(fileName)).normalize().toAbsolutePath();

        try (InputStream inputStream = content) {
            Files.copy(inputStream, destinationFile, options);

        } catch (FileAlreadyExistsException e) {
            throw new IllegalArgumentException("File already exists: " + destinationFile.toString(), e);
        } catch (IOException e) {
            throw InternalErrorException.with("Failed to write file.", e);
        }

    }

}
