package com.arqivame.storage.application.usecase.file.session.upload.create;

import java.util.UUID;

import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.service.ChunkCalculatorService;

public record CreateUploadSessionOutput(
        UUID fileId,
        Integer totalChunks,
        Long chunkSize,
        Long lastChunkSize) {

    public static CreateUploadSessionOutput from(
            final FileID fileId,
            final ChunkCalculatorService.ChunkCalculationResult chunkCalculationResult) {
        return new CreateUploadSessionOutput(
                fileId.getValue(),
                chunkCalculationResult.totalChunks(),
                chunkCalculationResult.chunkSize(),
                chunkCalculationResult.lastChunkSize());
    }

}
