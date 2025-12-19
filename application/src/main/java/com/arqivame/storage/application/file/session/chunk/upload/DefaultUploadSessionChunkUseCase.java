package com.arqivame.storage.application.file.session.chunk.upload;

import java.util.Objects;

import com.arqivame.storage.domain.event.EventDispatcher;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;

public class DefaultUploadSessionChunkUseCase extends UploadSessionChunkUseCase {

    private final FileGateway fileGateway;

    private final EventDispatcher eventDispatcher;

    public DefaultUploadSessionChunkUseCase(final FileGateway fileGateway, final EventDispatcher eventDispatcher) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    @Override
    public void execute(final UploadSessionChunkInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final File file = fileGateway.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + input.fileId()));

        final Checksum checksum = Checksum
                .from(
                        input.chunk().checksumValue(),
                        input.chunk().algorithm());

        final Chunk chunk = Chunk.create(checksum, input.chunk().index(), input.chunk().data());

        eventDispatcher.notify(fileGateway.save(file.appendChunk(chunk)));

    }

}
