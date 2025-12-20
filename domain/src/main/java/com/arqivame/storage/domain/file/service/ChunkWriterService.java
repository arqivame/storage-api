package com.arqivame.storage.domain.file.service;

import java.io.InputStream;

import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSession;
import com.arqivame.storage.domain.file.UploadSessionID;

public final class ChunkWriterService {

    private ChunkWriterService() {
    }

    public static File writeChunk(
            final File file,
            final UploadSessionID sessionId,
            final Long chunkIndex,
            final Checksum checksumValue,
            final InputStream chunkData,
            final StorageService writer) {

        final UploadSession session = file.fetchUploadSessionById(sessionId);

        session.initiateChunkWriting(chunkIndex, writer);
        session.writeChunk(chunkIndex, checksumValue, chunkData);

        return file;

    }

}
