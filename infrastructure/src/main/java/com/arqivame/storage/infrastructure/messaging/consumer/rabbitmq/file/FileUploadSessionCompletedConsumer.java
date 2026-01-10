package com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.file;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.LongStream;

import org.springframework.messaging.Message;

import com.arqivame.storage.application.service.file.FinalizerFileProcessingService;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.Session;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCompletedMessage;
import com.arqivame.storage.infrastructure.file.service.FileAssembler;
import com.arqivame.storage.infrastructure.file.service.FileAssembler.ChunkInfo;
import com.arqivame.storage.infrastructure.file.service.FileAssembler.MergeResult;
import com.arqivame.storage.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.storage.infrastructure.messaging.producer.MessageProducer;
import com.arqivame.storage.infrastructure.storage.service.StorageKey;

public class FileUploadSessionCompletedConsumer extends RabbitMQMessageConsumer<FileUploadSessionCompletedMessage> {

    private final FileGateway fileGateway;

    private final FileAssembler fileAssembler;

    private final FinalizerFileProcessingService finalizerFileProcessingService;

    public FileUploadSessionCompletedConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<FileUploadSessionCompletedMessage> errorMessageProducer,
            final FileGateway fileGateway,
            final FileAssembler fileAssembler,
            final FinalizerFileProcessingService finalizerFileProcessingService) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.fileAssembler = Objects.requireNonNull(fileAssembler);
        this.finalizerFileProcessingService = Objects.requireNonNull(finalizerFileProcessingService);
    }

    @Override
    public void consume(final Message<FileUploadSessionCompletedMessage> message) {

        final FileUploadSessionCompletedMessage event = message.getPayload();
        final FileUploadSessionCompletedMessage.Data data = event.data();

        final File file = fileGateway
                .findById(FileID.of(data.fileId()))
                .orElseThrow(); // TODO exception

        final Session uploadSession = file
                .getUploadSession()
                .orElseThrow(); // TODO exception

        final Set<ChunkInfo> chunks = new HashSet<>();

        LongStream.range(0, uploadSession.totalChunks())
                .forEach(index -> chunks.add(
                        ChunkInfo.with(StorageKey.create("files", file.getId().getStringValue())
                                .subKey("upload", "chunks", String.valueOf(index)), index)));

        final Long firstChunkSize = uploadSession.chunkSize();

        final StorageKey fileStorageKey = StorageKey.create("files", file.getId().getStringValue()).subKey("data");
        final MergeResult mergeResult = fileAssembler.mergeChunks(fileStorageKey, chunks, firstChunkSize);

        finalizerFileProcessingService.finalize(file.getId().getValue(), mergeResult.allChunksMerged());

    }

}
