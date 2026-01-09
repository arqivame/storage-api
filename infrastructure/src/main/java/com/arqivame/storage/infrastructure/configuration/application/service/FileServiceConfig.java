package com.arqivame.storage.infrastructure.configuration.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.service.file.ChunkCleanerService;
import com.arqivame.storage.application.service.file.FinalizeFileProcessingService;
import com.arqivame.storage.application.service.storage.FileAssemblerService;
import com.arqivame.storage.application.service.storage.StorageDeleter;
import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.FileGateway;

@Configuration
public class FileServiceConfig {

    private final EventDispatcher eventDispatcher;
    private final FileGateway fileGateway;
    private final FileAssemblerService fileAssemblerService;
    private final StorageDeleter storageDeleter;

    public FileServiceConfig(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway,
            final FileAssemblerService fileAssemblerService,
            final StorageDeleter storageDeleter) {
        this.eventDispatcher = eventDispatcher;
        this.fileGateway = fileGateway;
        this.fileAssemblerService = fileAssemblerService;
        this.storageDeleter = storageDeleter;
    }

    @Bean
    FinalizeFileProcessingService finalizeFileProcessingService() {
        return new FinalizeFileProcessingService(
                eventDispatcher,
                fileGateway,
                fileAssemblerService);
    }

    @Bean
    ChunkCleanerService chunkCleanerService() {
        return new ChunkCleanerService(fileGateway, storageDeleter);
    }

}
