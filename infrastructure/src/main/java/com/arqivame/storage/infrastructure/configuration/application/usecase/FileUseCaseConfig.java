package com.arqivame.storage.infrastructure.configuration.application.usecase;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.port.ConcurrencyTracker;
import com.arqivame.storage.application.usecase.file.chunk.upload.DefaultUploadChunkUseCase;
import com.arqivame.storage.application.usecase.file.chunk.upload.UploadChunkUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.abort.AbortUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.abort.DefaultAbortUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.complete.CompleteUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.complete.DefaultCompleteUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.create.CreateUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.create.DefaultCreateUploadSessionUseCase;
import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.service.StorageService;

@Configuration
public class FileUseCaseConfig {

    private final FileGateway fileGateway;

    private final StorageService storageService;

    private final ConcurrencyTracker concurrencyTracker;

    private final EventDispatcher eventDispatcher;

    public FileUseCaseConfig(
            final FileGateway fileGateway,
            final StorageService storageService,
            final ConcurrencyTracker concurrencyTracker,
            final EventDispatcher eventDispatcher) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageService = Objects.requireNonNull(storageService);
        this.concurrencyTracker = Objects.requireNonNull(concurrencyTracker);
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
    CompleteUploadSessionUseCase completeUploadSessionUseCase() {
        return new DefaultCompleteUploadSessionUseCase(eventDispatcher, fileGateway);
    }

    @Bean
    AbortUploadSessionUseCase abortUploadSessionUseCase() {
        return new DefaultAbortUploadSessionUseCase(eventDispatcher, fileGateway);
    }

    @Bean
    UploadChunkUseCase uploadChunkUseCase() {
        return new DefaultUploadChunkUseCase(fileGateway, storageService, concurrencyTracker);
    }

}
