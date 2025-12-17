package com.arqivame.storage.infrastructure.configuration.application.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.file.session.chunk.upload.DefaultUploadSessionChunkUseCase;
import com.arqivame.storage.application.file.session.chunk.upload.UploadSessionChunkUseCase;
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
    UploadSessionChunkUseCase uploadSessionChunkUseCase() {
        return new DefaultUploadSessionChunkUseCase(fileGateway, eventDispatcher);
    }

}
