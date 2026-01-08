package com.arqivame.storage.application.service.file;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.exception.MergeChunksException;
import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.service.FileAssemblerService;
import com.arqivame.storage.domain.file.service.FileAssemblerService.ChunkInfo;
import com.arqivame.storage.domain.file.service.FileAssemblerService.MergeResult;
import com.arqivame.storage.domain.file.service.StorageKey;

public class FinalizeFileProcessingService {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;

    private final FileAssemblerService fileAssemblerService;

    public FinalizeFileProcessingService(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway,
            final FileAssemblerService fileAssemblerService) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.fileAssemblerService = Objects.requireNonNull(fileAssemblerService);
    }

    public void finalizeFileProcessing(final UUID fileId) {

        final File file = fileGateway
                .findById(FileID.of(fileId))
                .orElseThrow(); // TODO exception

        final Set<Chunk> uploadedChunks = file.getUploadedChunks();

        final Long firstChunkSize = uploadedChunks
                .stream()
                .filter(chunk -> Long.valueOf(0L).equals(chunk.index()))
                .map(Chunk::size)
                .findAny()
                .orElseThrow();// TODO exception

        final Set<ChunkInfo> chunks = uploadedChunks
                .stream()
                .map(chunk -> ChunkInfo.with(
                        file.getStorageKey().subKey("upload", "chunks", chunk.index().toString()),
                        chunk))
                .collect(Collectors.toSet());

        final StorageKey fileStorageKey = file.getStorageKey().subKey("data");
        final MergeResult mergeResult = fileAssemblerService.mergeChunks(fileStorageKey, chunks, firstChunkSize);
        if (!mergeResult.allChunksMerged())
            throw MergeChunksException.create();

        eventDispatcher.notify(fileGateway.update(file.markAsAvailable()));

    }

}
