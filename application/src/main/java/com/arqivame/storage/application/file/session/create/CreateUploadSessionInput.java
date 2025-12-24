package com.arqivame.storage.application.file.session.create;

import java.util.UUID;

import com.arqivame.storage.domain.file.Checksum.Algorithm;

public record CreateUploadSessionInput(
                UUID fileId,
                Long fileSize,
                Long idleTimeoutSeconds,
                Integer maxChunksAtSameTime,
                Long maxBytesPerSecondTransferRatePerChunk,
                String checksumValue,
                Algorithm checksumAlgorithm) {

}
