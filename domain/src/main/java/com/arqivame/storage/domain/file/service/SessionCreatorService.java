package com.arqivame.storage.domain.file.service;

import java.time.Duration;

import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSessionID;

public final class SessionCreatorService {

    private SessionCreatorService() {
    }

    public static UploadSessionID createSession(
            final File file,
            final Long totalChunks,
            final Long chunkSize,
            final Long lastChunkSize,
            final Duration maxIdleTime,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime) {

        return file.openUploadSession(
                totalChunks,
                chunkSize,
                lastChunkSize,
                maxIdleTime,
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime).getId();

    }

}
