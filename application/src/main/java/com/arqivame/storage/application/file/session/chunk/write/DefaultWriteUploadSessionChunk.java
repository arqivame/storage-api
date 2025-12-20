package com.arqivame.storage.application.file.session.chunk.write;

import java.io.InputStream;
import java.util.Objects;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;
import com.arqivame.storage.domain.file.service.ChunkWriterService;
import com.arqivame.storage.domain.file.service.StorageService;

public class DefaultWriteUploadSessionChunk extends WriteUploadSessionChunk {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;
    private final StorageService inputStreamWriter;

    public DefaultWriteUploadSessionChunk(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway,
            final StorageService inputStreamWriter) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.inputStreamWriter = Objects.requireNonNull(inputStreamWriter);
    }

    @Override
    public void execute(final WriteUploadSessionChunkInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final UploadSessionID sessionId = UploadSessionID.of(input.uploadSessionId());
        final Long chunkIndex = input.chunkIndex();
        final Checksum checksumValue = Checksum.from(input.checksumValue(), input.checksumAlgorithm());
        final InputStream chunkData = input.chunkData();

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: " + input.fileId()));

        eventDispatcher.notify(
                fileGateway.save(
                        ChunkWriterService.writeChunk(
                                file,
                                sessionId,
                                chunkIndex,
                                checksumValue,
                                chunkData,
                                inputStreamWriter)));

    }

}
