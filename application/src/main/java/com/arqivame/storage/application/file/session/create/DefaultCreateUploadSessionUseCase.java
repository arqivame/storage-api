package com.arqivame.storage.application.file.session.create;

import java.time.Duration;
import java.util.Objects;

import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.service.ChunkCalculatorService;
import com.arqivame.storage.domain.file.service.SessionCreatorService;

public class DefaultCreateUploadSessionUseCase extends CreateUploadSessionUseCase {

    private final Long maxAllowedChunkSize;
    private final FileGateway fileGateway;

    public DefaultCreateUploadSessionUseCase(
            final Long maxAllowedChunkSize,
            final FileGateway fileGateway) {
        this.maxAllowedChunkSize = Objects.requireNonNull(maxAllowedChunkSize);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public CreateUploadSessionOutput execute(final CreateUploadSessionInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final Long fileSize = input.fileSize();
        final Duration idleTimeout = input.idleTimeout();
        final Integer maxChunksAtSameTime = input.maxChunksAtSameTime();
        final Checksum checksum = Checksum.from(input.checksumValue(), input.checksumAlgorithm());

        final File file = fileGateway
                .findById(fileId)
                .orElseGet(() -> createFile(fileId, fileSize, checksum));

        final var chunkCalculationResult = ChunkCalculatorService.calculate(maxAllowedChunkSize, fileSize);

        // chunkCalculationResult.totalChunks();
        chunkCalculationResult.chunkSize();
        chunkCalculationResult.lastChunkSize();

        SessionCreatorService.createSession(
                file,
                chunkCalculationResult.totalChunks(),
                idleTimeout,
                fileSize,
                maxChunksAtSameTime);

        fileGateway.save(file);

        return null;

    }

    private static File createFile(
            final FileID fileId,
            final Long fileSize,
            final Checksum checksum) {
        return File.create(fileId, fileSize, checksum);
    }

}
