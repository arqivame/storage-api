package com.arqivame.storage.infrastructure.configuration.application.usecase;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.port.ChunkReader;
import com.arqivame.storage.application.port.ChunkWriter;
import com.arqivame.storage.application.port.ConcurrencyTracker;
import com.arqivame.storage.application.usecase.file.chunk.download.DefaultDownloadChunkUseCase;
import com.arqivame.storage.application.usecase.file.chunk.download.DownloadChunkUseCase;
import com.arqivame.storage.application.usecase.file.chunk.upload.DefaultUploadChunkUseCase;
import com.arqivame.storage.application.usecase.file.chunk.upload.UploadChunkUseCase;
import com.arqivame.storage.application.usecase.file.session.download.create.CreateDownloadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.download.create.DefaultCreateDownloadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.abort.AbortUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.abort.DefaultAbortUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.complete.CompleteUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.complete.DefaultCompleteUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.create.CreateUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.create.DefaultCreateUploadSessionUseCase;
import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.FileGateway;

@Configuration
public class FileUseCaseConfig {

    private final FileGateway fileGateway;

    private final ConcurrencyTracker concurrencyTracker;
    private final ChunkWriter chunkWriter;
    private final ChunkReader chunkReader;

    private final EventDispatcher eventDispatcher;

    public FileUseCaseConfig(
            final FileGateway fileGateway,
            final ConcurrencyTracker concurrencyTracker,
            final ChunkWriter chunkWriter,
            final ChunkReader chunkReader,
            final EventDispatcher eventDispatcher) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.concurrencyTracker = Objects.requireNonNull(concurrencyTracker);
        this.chunkWriter = Objects.requireNonNull(chunkWriter);
        this.chunkReader = Objects.requireNonNull(chunkReader);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    @Bean
    CreateUploadSessionUseCase createUploadSessionUseCase() {
        return new DefaultCreateUploadSessionUseCase(
                eventDispatcher,
                1024L * 1024L * 250L, // 250 MB
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
        return new DefaultUploadChunkUseCase(fileGateway, chunkWriter, concurrencyTracker);
    }

    @Bean
    CreateDownloadSessionUseCase createDownloadSessionUseCase() {
        return new DefaultCreateDownloadSessionUseCase(
                eventDispatcher,
                1024L * 1024L * 20L, // 20 MB
                fileGateway);
    }

    @Bean
    DownloadChunkUseCase downloadChunkUseCase() {
        return new DefaultDownloadChunkUseCase(fileGateway, chunkReader, concurrencyTracker);
    }

}
