package com.arqivame.storage.domain.file.service;

import java.time.Duration;

import com.arqivame.storage.domain.file.File;

public final class SessionCreatorService {

    private SessionCreatorService() {
    }

    public static File createSession(
            final File file,
            final Long totalChunks,
            final Duration maxIdleTime,
            final Long maxBitsPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime) {

        return file.openUploadSession(
                totalChunks,
                maxIdleTime,
                maxBitsPerSecondTransferRatePerChunk,
                maxChunksAtSameTime);

    }

}
