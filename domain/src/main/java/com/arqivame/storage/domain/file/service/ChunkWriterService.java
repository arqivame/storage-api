package com.arqivame.storage.domain.file.service;

import java.io.InputStream;

import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSessionID;

public final class ChunkWriterService {

    private ChunkWriterService() {
    }

    public static File initiateChunkWriting(
            final File file,
            final UploadSessionID sessionId,
            final Long chunkIndex,
            final StorageService writer) {

        file.fetchUploadSessionById(sessionId).initiateChunkWriting(chunkIndex, writer);

        return file;

    }

    public static File writeChunk(
            final File file,
            final UploadSessionID sessionId,
            final Long chunkIndex,
            final Checksum checksumValue,
            final InputStream chunkData) {

        file.fetchUploadSessionById(sessionId).writeChunk(chunkIndex, checksumValue, chunkData);

        return file;

    }

}
