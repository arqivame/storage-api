package com.arqivame.storage.infrastructure.configuration.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.service.file.FinalizerFileProcessingService;
import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.FileGateway;

@Configuration
public class FileServiceConfig {

    private final EventDispatcher eventDispatcher;
    private final FileGateway fileGateway;

    public FileServiceConfig(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway) {
        this.eventDispatcher = eventDispatcher;
        this.fileGateway = fileGateway;

    }

    @Bean
    FinalizerFileProcessingService finalizerFileProcessingService() {
        return new FinalizerFileProcessingService(eventDispatcher, fileGateway);
    }

}
