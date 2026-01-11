package com.arqivame.storage.application.usecase.file.chunk.download;

import java.util.Objects;

import com.arqivame.storage.application.exception.MaxConcurrentChunkReadsReachedException;
import com.arqivame.storage.application.port.ChunkReader;
import com.arqivame.storage.application.port.ConcurrencyTracker;
import com.arqivame.storage.domain.exception.DomainException.Error;
import com.arqivame.storage.domain.exception.InvalidArgumentException;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.Session;

public class DefaultDownloadChunkUseCase extends DownloadChunkUseCase {

    private static final String CONCURRENCY_TAG_DOWNLOAD = "download";

    private final FileGateway fileGateway;

    private final ChunkReader chunkReader;

    private final ConcurrencyTracker concurrencyTracker;

    public DefaultDownloadChunkUseCase(
            final FileGateway fileGateway,
            final ChunkReader chunkReader,
            final ConcurrencyTracker concurrencyTracker) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.chunkReader = Objects.requireNonNull(chunkReader);
        this.concurrencyTracker = Objects.requireNonNull(concurrencyTracker);
    }

    @Override
    public DownloadChunkOutput execute(final DownloadChunkInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final Long chunkIndex = input.chunkIndex();

        final File file = findFileById(fileId);
        final Session downloadSession = file.getDownloadSession().orElseThrow(); // TODO exception
        final Long bytesPerSecondsRate = downloadSession.maxBytesPerSecondTransferRatePerChunk();
        final Long chunkOffset = calculateOffset(file.getSize(), downloadSession, chunkIndex);
        final Long chunkSize = calculateChunkSizeByIndex(downloadSession, chunkIndex);

        final Integer currentConcurrency = concurrencyTracker.getCurrentCount(fileId, CONCURRENCY_TAG_DOWNLOAD);
        if (currentConcurrency >= downloadSession.maxChunksAtSameTime())
            throw MaxConcurrentChunkReadsReachedException.create(downloadSession.maxChunksAtSameTime());
        concurrencyTracker.increment(fileId, CONCURRENCY_TAG_DOWNLOAD);

        try {

            return new DownloadChunkOutput(chunkReader.readChunk(fileId, chunkOffset, chunkSize, bytesPerSecondsRate));

        } finally {
            concurrencyTracker.decrement(fileId, CONCURRENCY_TAG_DOWNLOAD);
        }

    }

    private File findFileById(final FileID fileId) {
        return fileGateway
                .findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: " + fileId.getValue()));
    }

    private static Long calculateChunkSizeByIndex(Session uploadSession, Long chunkIndex) {

        if (chunkIndex < 0 || chunkIndex >= uploadSession.totalChunks())
            throw InvalidArgumentException.with(Error.with("Chunk index out of bounds: " + chunkIndex));

        return (chunkIndex == uploadSession.totalChunks() - 1) ? uploadSession.lastChunkSize()
                : uploadSession.chunkSize();

    }

    private static Long calculateOffset(final Long fileSize, final Session downloadSession, final Long chunkIndex) {

        if (chunkIndex < 0 || chunkIndex >= downloadSession.totalChunks())
            throw InvalidArgumentException.with(Error.with("Chunk index out of bounds: " + chunkIndex));

        if (chunkIndex == downloadSession.totalChunks() - 1)
            return (fileSize - downloadSession.lastChunkSize());

        return (chunkIndex * downloadSession.chunkSize());

    }

}
