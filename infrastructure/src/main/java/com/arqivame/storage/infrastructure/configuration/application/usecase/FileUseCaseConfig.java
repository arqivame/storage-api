package com.arqivame.storage.infrastructure.configuration.application.usecase;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.file.session.cancel.CancelUploadSessionUseCase;
import com.arqivame.storage.application.file.session.cancel.DefaultCancelUploadSessionUseCase;
import com.arqivame.storage.application.file.session.chunk.write.DefaultWriteUploadSessionChunkUseCase;
import com.arqivame.storage.application.file.session.chunk.write.WriteUploadSessionChunkUseCase;
import com.arqivame.storage.application.file.session.create.CreateUploadSessionUseCase;
import com.arqivame.storage.application.file.session.create.DefaultCreateUploadSessionUseCase;
import com.arqivame.storage.application.file.session.delete.mark.DefaultMarkUploadSessionForDeletionUseCase;
import com.arqivame.storage.application.file.session.delete.mark.MarkUploadSessionForDeletionUseCase;
import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.service.StorageWriter;

@Configuration
public class FileUseCaseConfig {

    private final FileGateway fileGateway;

    private final StorageWriter storageService;

    private final EventDispatcher eventDispatcher;

    public FileUseCaseConfig(
            final FileGateway fileGateway,
            final StorageWriter storageService,
            final EventDispatcher eventDispatcher) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageService = Objects.requireNonNull(storageService);
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

}
