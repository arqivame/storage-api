package com.arqivame.storage.application.file.session.chunk.merge;

import java.util.Objects;
import java.util.Set;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.exception.MergeChunksException;
import com.arqivame.storage.domain.exception.NotFoundException;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSession;
import com.arqivame.storage.domain.file.UploadSessionID;
import com.arqivame.storage.domain.file.service.ChunkMergeService;
import com.arqivame.storage.domain.file.service.ChunkMergeService.MergeResult;
import com.arqivame.storage.domain.file.service.StorageKey;

public class DefaultMergeUploadSessionChunksUseCase extends MergeUploadSessionChunksUseCase {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;
    private final ChunkMergeService chunkMergeService;

    public DefaultMergeUploadSessionChunksUseCase(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway,
            final ChunkMergeService chunkMergeService) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.chunkMergeService = Objects.requireNonNull(chunkMergeService);
    }

    @Override
    public void execute(final MergeUploadSessionChunksInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final UploadSessionID sessionId = UploadSessionID.of(input.uploadSessionId());

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        final UploadSession uploadSession = file.fetchUploadSessionById(sessionId);

        final StorageKey finalFileKey = StorageKey.from(fileId);
        final Set<ChunkMergeService.ChunkInfo> chunks = uploadSession
                .getChunks()
                .stream()
                .map(ChunkMergeService.ChunkInfo::with)
                .collect(java.util.stream.Collectors.toSet());
        final Long firstChunkSize = uploadSession.getChunkSize();

        final MergeResult mergeResult = chunkMergeService.mergeChunks(finalFileKey, chunks, firstChunkSize);

        if (!mergeResult.allChunksMerged())
            throw MergeChunksException.create();

        eventDispatcher.notify(
                fileGateway.update(
                        file.completeUploadSessionProcessing(sessionId).markUploadSessionForDeletion(sessionId)));

    }

}
