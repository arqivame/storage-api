package com.arqivame.storage.application.usecase.file.session.download.create;

import java.util.UUID;

import com.arqivame.storage.application.service.file.ChunkPartitioningService;
import com.arqivame.storage.domain.file.FileID;

public record CreateDownloadSessionOutput(
        UUID fileId,
        Integer totalChunks,
        Long chunkSize,
        Long lastChunkSize) {

    public static CreateDownloadSessionOutput from(
            final FileID fileId,
            final ChunkPartitioningService.ChunkCalculationResult chunkCalculationResult) {
        return new CreateDownloadSessionOutput(
                fileId.getValue(),
                chunkCalculationResult.totalChunks(),
                chunkCalculationResult.chunkSize(),
                chunkCalculationResult.lastChunkSize());
    }

}
