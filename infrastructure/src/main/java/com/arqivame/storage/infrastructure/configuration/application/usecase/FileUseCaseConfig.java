package com.arqivame.storage.infrastructure.configuration.application.usecase;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.file.session.chunk.write.DefaultWriteUploadSessionChunkUseCase;
import com.arqivame.storage.application.file.session.chunk.write.WriteUploadSessionChunkUseCase;
import com.arqivame.storage.application.file.session.create.CreateUploadSessionUseCase;
import com.arqivame.storage.application.file.session.create.DefaultCreateUploadSessionUseCase;
import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.service.StorageService;

@Configuration
public class FileUseCaseConfig {

    private final FileGateway fileGateway;

    private final StorageService storageService;

    private final EventDispatcher eventDispatcher;

    public FileUseCaseConfig(
            final FileGateway fileGateway,
            final StorageService storageService,
            final EventDispatcher eventDispatcher) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageService = Objects.requireNonNull(storageService);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    @Bean
    CreateUploadSessionUseCase createUploadSessionUseCase() {
        return new DefaultCreateUploadSessionUseCase(
                eventDispatcher,
                10L,
                fileGateway);
    }

    @Bean
    WriteUploadSessionChunkUseCase writeUploadSessionChunkUseCase() {
        return new DefaultWriteUploadSessionChunkUseCase(
                eventDispatcher,
                fileGateway,
                storageService);
    }

}
