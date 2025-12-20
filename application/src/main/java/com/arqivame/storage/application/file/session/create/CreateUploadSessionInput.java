package com.arqivame.storage.application.file.session.create;

import java.time.Duration;
import java.util.UUID;

import com.arqivame.storage.domain.file.Checksum.Algorithm;

public record CreateUploadSessionInput(
                UUID fileId,
                Long fileSize,
                Duration idleTimeout,
                Integer maxChunksAtSameTime,
                String checksumValue,
                Algorithm checksumAlgorithm) {

}
