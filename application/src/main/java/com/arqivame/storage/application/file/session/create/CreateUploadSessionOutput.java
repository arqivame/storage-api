package com.arqivame.storage.application.file.session.create;

import java.util.UUID;

import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;
import com.arqivame.storage.domain.file.service.ChunkCalculatorService;

public record CreateUploadSessionOutput(
        UUID fileId,
        UUID uploadSessionId,
        Long totalChunks,
        Long chunkSize,
        Long lastChunkSize) {

    public static CreateUploadSessionOutput from(
            final FileID fileId,
            final UploadSessionID uploadSessionId,
            final ChunkCalculatorService.ChunkCalculationResult chunkCalculationResult) {
        return new CreateUploadSessionOutput(
                fileId.getValue(),
                uploadSessionId.getValue(),
                chunkCalculationResult.totalChunks(),
                chunkCalculationResult.chunkSize(),
                chunkCalculationResult.lastChunkSize());
    }

}
