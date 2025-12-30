package com.arqivame.storage.infrastructure.configuration.application.usecase;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.file.session.cancel.CancelUploadSessionUseCase;
import com.arqivame.storage.application.file.session.cancel.DefaultCancelUploadSessionUseCase;
import com.arqivame.storage.application.file.session.chunk.merge.DefaultMergeUploadSessionChunksUseCase;
import com.arqivame.storage.application.file.session.chunk.merge.MergeUploadSessionChunksUseCase;
import com.arqivame.storage.application.file.session.chunk.write.DefaultWriteUploadSessionChunkUseCase;
import com.arqivame.storage.application.file.session.chunk.write.WriteUploadSessionChunkUseCase;
import com.arqivame.storage.application.file.session.create.CreateUploadSessionUseCase;
import com.arqivame.storage.application.file.session.create.DefaultCreateUploadSessionUseCase;
import com.arqivame.storage.application.file.session.delete.mark.DefaultMarkUploadSessionForDeletionUseCase;
import com.arqivame.storage.application.file.session.delete.mark.MarkUploadSessionForDeletionUseCase;
import com.arqivame.storage.application.file.session.delete.physical.DefaultPhysicalUploadSessionDeleteUseCase;
import com.arqivame.storage.application.file.session.delete.physical.PhysicalUploadSessionDeleteUseCase;
import com.arqivame.storage.application.file.session.process.initiate.DefaultInitiateUploadSessionProcessingUseCase;
import com.arqivame.storage.application.file.session.process.initiate.InitiateUploadSessionProcessingUseCase;
import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.service.ChunkMergeService;
import com.arqivame.storage.domain.file.service.StorageService;

@Configuration
public class FileUseCaseConfig {

    private final FileGateway fileGateway;

    private final StorageService storageService;
    private final ChunkMergeService chunkMergeService;

    private final EventDispatcher eventDispatcher;

    public FileUseCaseConfig(
            final FileGateway fileGateway,
            final StorageService storageService,
            final ChunkMergeService chunkMergeService,
            final EventDispatcher eventDispatcher) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageService = Objects.requireNonNull(storageService);
        this.chunkMergeService = Objects.requireNonNull(chunkMergeService);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    @Bean
    CreateUploadSessionUseCase createUploadSessionUseCase() {
        return new DefaultCreateUploadSessionUseCase(
                eventDispatcher,
                1024L * 1024L * 20L, // 20 MB
                fileGateway);
    }

    @Bean
    WriteUploadSessionChunkUseCase writeUploadSessionChunkUseCase() {
        return new DefaultWriteUploadSessionChunkUseCase(
                eventDispatcher,
                fileGateway,
                storageService);
    }

    @Bean
    CancelUploadSessionUseCase cancelUploadSessionUseCase() {
        return new DefaultCancelUploadSessionUseCase(
                eventDispatcher,
                fileGateway);
    }

    @Bean
    MarkUploadSessionForDeletionUseCase markUploadSessionForDeletionUseCase() {
        return new DefaultMarkUploadSessionForDeletionUseCase(eventDispatcher, fileGateway);
    }

    @Bean
    PhysicalUploadSessionDeleteUseCase physicalUploadSessionDeleteUseCase() {
        return new DefaultPhysicalUploadSessionDeleteUseCase(
                eventDispatcher,
                fileGateway,
                storageService);
    }

    @Bean
    InitiateUploadSessionProcessingUseCase initiateUploadSessionProcessingUseCase() {
        return new DefaultInitiateUploadSessionProcessingUseCase(
                eventDispatcher,
                fileGateway);
    }

    @Bean
    MergeUploadSessionChunksUseCase mergeUploadSessionChunksUseCase() {
        return new DefaultMergeUploadSessionChunksUseCase(
                eventDispatcher,
                fileGateway,
                chunkMergeService);
    }

}
