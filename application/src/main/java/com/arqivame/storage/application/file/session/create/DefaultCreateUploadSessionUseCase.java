package com.arqivame.storage.application.file.session.create;

import java.time.Duration;
import java.util.Objects;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;
import com.arqivame.storage.domain.file.service.ChunkCalculatorService;

public class DefaultCreateUploadSessionUseCase extends CreateUploadSessionUseCase {

    private final EventDispatcher eventDispatcher;

    private final Long maxAllowedChunkSize;
    private final FileGateway fileGateway;

    public DefaultCreateUploadSessionUseCase(
            final EventDispatcher eventDispatcher,
            final Long maxAllowedChunkSize,
            final FileGateway fileGateway) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.maxAllowedChunkSize = Objects.requireNonNull(maxAllowedChunkSize);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public CreateUploadSessionOutput execute(final CreateUploadSessionInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final Long fileSize = input.fileSize();
        final Duration idleTimeout = Duration.ofSeconds(input.idleTimeoutSeconds());
        final Integer maxChunksAtSameTime = input.maxChunksAtSameTime();
        final Long maxBytesPerSecondTransferRatePerChunk = input.maxBytesPerSecondTransferRatePerChunk();
        final Checksum checksum = Checksum.from(input.checksumValue(), input.checksumAlgorithm());

        final File file = fileGateway
                .findById(fileId)
                .orElseGet(() -> createFile(fileId, fileSize, checksum));

        final var chunkCalculationResult = ChunkCalculatorService.calculate(maxAllowedChunkSize, fileSize);

        final UploadSessionID sessionId = file.openUploadSession(
                chunkCalculationResult.totalChunks(),
                chunkCalculationResult.chunkSize(),
                chunkCalculationResult.lastChunkSize(),
                idleTimeout,
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime);

        eventDispatcher.notify(fileGateway.create(file));

        return CreateUploadSessionOutput.from(fileId, sessionId, chunkCalculationResult);

    }

    private static File createFile(
            final FileID fileId,
            final Long fileSize,
            final Checksum checksum) {
        return File.create(fileId, fileSize, checksum);
    }

}
