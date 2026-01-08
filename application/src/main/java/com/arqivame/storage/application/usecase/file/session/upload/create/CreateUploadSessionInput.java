package com.arqivame.storage.application.usecase.file.session.upload.create;

import java.util.UUID;

import com.arqivame.storage.domain.file.Checksum.Algorithm;

public record CreateUploadSessionInput(
        UUID fileId,
        Long fileSize,
        Integer maxChunksAtSameTime,
        Long maxBytesPerSecondTransferRatePerChunk,
        String checksumValue,
        Algorithm checksumAlgorithm) {

}
