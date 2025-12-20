package com.arqivame.storage.domain.file.service;

import java.io.InputStream;

import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSession;
import com.arqivame.storage.domain.file.UploadSessionID;

public final class ChunkWriterService {

    private ChunkWriterService() {
    }

    public static File writeChunk(
            final File file,
            final UploadSessionID sessionId,
            final Integer chunkIndex,
            final InputStream chunkData,
            final InputStreamWriter writer) {

        final UploadSession session = file.fetchUploadSessionById(sessionId);

        session.markChunkAsWriting(chunkIndex);
        session.writeChunk(chunkIndex, chunkData, writer); // Maybe needs checksum here

        return file;

    }

}
