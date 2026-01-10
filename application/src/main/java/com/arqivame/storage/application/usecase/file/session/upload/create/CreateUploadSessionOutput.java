package com.arqivame.storage.application.usecase.file.session.upload.create;

import java.util.UUID;

import com.arqivame.storage.application.service.file.ChunkPartitioningService;
import com.arqivame.storage.domain.file.FileID;

public record CreateUploadSessionOutput(
        UUID fileId,
        Integer totalChunks,
        Long chunkSize,
        Long lastChunkSize) {

    public static CreateUploadSessionOutput from(
            final FileID fileId,
            final ChunkPartitioningService.ChunkCalculationResult chunkCalculationResult) {
        return new CreateUploadSessionOutput(
                fileId.getValue(),
                chunkCalculationResult.totalChunks(),
                chunkCalculationResult.chunkSize(),
                chunkCalculationResult.lastChunkSize());
    }

}
