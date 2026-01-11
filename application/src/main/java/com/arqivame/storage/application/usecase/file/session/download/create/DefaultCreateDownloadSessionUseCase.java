package com.arqivame.storage.application.usecase.file.session.download.create;

import java.util.Objects;

import com.arqivame.storage.application.service.file.ChunkPartitioningService;
import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.exception.NotFoundException;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;

public class DefaultCreateDownloadSessionUseCase extends CreateDownloadSessionUseCase {

    private final EventDispatcher eventDispatcher;

    private final Long maxAllowedChunkSize;

    private final FileGateway fileGateway;

    public DefaultCreateDownloadSessionUseCase(
            final EventDispatcher eventDispatcher,
            final Long maxAllowedChunkSize,
            final FileGateway fileGateway) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.maxAllowedChunkSize = Objects.requireNonNull(maxAllowedChunkSize);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public CreateDownloadSessionOutput execute(final CreateDownloadSessionInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final Integer maxChunksAtSameTime = input.maxChunksAtSameTime();
        final Long maxBytesPerSecondTransferRatePerChunk = input.maxBytesPerSecondTransferRatePerChunk();

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        final var chunkCalculationResult = ChunkPartitioningService.calculate(maxAllowedChunkSize, file.getSize());

        file.openDownloadSession(
                chunkCalculationResult.totalChunks(),
                chunkCalculationResult.chunkSize(),
                chunkCalculationResult.lastChunkSize(),
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime);

        eventDispatcher.notify(fileGateway.update(file));

        return CreateDownloadSessionOutput.from(fileId, chunkCalculationResult);

    }

}
