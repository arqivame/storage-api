package com.arqivame.storage.application.usecase.file.chunk.upload;

import java.io.InputStream;
import java.util.Objects;

import com.arqivame.storage.application.exception.ChunkIntegrityViolationException;
import com.arqivame.storage.application.exception.MaxConcurrentChunkWritesReachedException;
import com.arqivame.storage.application.port.ChunkWriter;
import com.arqivame.storage.application.port.ConcurrencyTracker;
import com.arqivame.storage.domain.exception.DomainException.Error;
import com.arqivame.storage.domain.exception.InvalidArgumentException;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.Session;

public class DefaultUploadChunkUseCase extends UploadChunkUseCase {

    private static final String CONCURRENCY_TAG_UPLOAD = "upload";

    private final FileGateway fileGateway;

    private final ChunkWriter chunkWriter;

    private final ConcurrencyTracker concurrencyTracker;

    public DefaultUploadChunkUseCase(
            final FileGateway fileGateway,
            final ChunkWriter chunkWriter,
            final ConcurrencyTracker concurrencyTracker) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.chunkWriter = Objects.requireNonNull(chunkWriter);
        this.concurrencyTracker = Objects.requireNonNull(concurrencyTracker);
    }

    @Override
    public void execute(final UploadChunkInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final Long chunkIndex = input.chunkIndex();
        final Checksum checksumValue = Checksum.from(input.checksumValue(), input.checksumAlgorithm());
        final InputStream chunkData = input.chunkData();

        final File file = findFileById(fileId);
        final Session uploadSession = file.getUploadSession().orElseThrow();// TODO exception
        final Long chunkSize = calculateChunkSizeByIndex(uploadSession, chunkIndex);

        final Integer currentConcurrency = concurrencyTracker.getCurrentCount(fileId, CONCURRENCY_TAG_UPLOAD);
        if (currentConcurrency >= uploadSession.maxChunksAtSameTime())
            throw MaxConcurrentChunkWritesReachedException.create(uploadSession.maxChunksAtSameTime());
        concurrencyTracker.increment(fileId, CONCURRENCY_TAG_UPLOAD);

        try {

            final Checksum writeResult = chunkWriter.writeChunk(
                    fileId,
                    chunkIndex,
                    chunkData,
                    chunkSize,
                    uploadSession.maxBytesPerSecondTransferRatePerChunk(),
                    checksumValue.algorithm());

            if (!writeResult.equals(checksumValue))
                throw ChunkIntegrityViolationException.create();

        } finally {
            concurrencyTracker.decrement(fileId, CONCURRENCY_TAG_UPLOAD);
        }

    }

    private File findFileById(final FileID fileId) {
        return fileGateway
                .findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: " + fileId.getValue()));
    }

    private Long calculateChunkSizeByIndex(Session uploadSession, Long chunkIndex) {

        if (chunkIndex < 0 || chunkIndex >= uploadSession.totalChunks())
            throw InvalidArgumentException.with(Error.with("Chunk index out of bounds: " + chunkIndex));

        return (chunkIndex == uploadSession.totalChunks() - 1) ? uploadSession.lastChunkSize()
                : uploadSession.chunkSize();

    }

}
