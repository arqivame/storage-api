package com.arqivame.storage.infrastructure.configuration.application.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.file.session.create.CreateUploadSessionUseCase;
import com.arqivame.storage.application.file.session.create.DefaultCreateUploadSessionUseCase;
import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.FileGateway;

@Configuration
public class FileUseCaseConfig {

    private final FileGateway fileGateway;

    private final EventDispatcher eventDispatcher;

    public FileUseCaseConfig(
            final FileGateway fileGateway,
            final EventDispatcher eventDispatcher) {
        this.fileGateway = fileGateway;
        this.eventDispatcher = eventDispatcher;
    }

    @Bean
    CreateUploadSessionUseCase createUploadSessionUseCase() {
        return new DefaultCreateUploadSessionUseCase(
                eventDispatcher,
                1024L * 1024L * 10L, // 10 MB
                fileGateway);
    }

}
