package com.arqivame.storage.application.file.session.chunk.upload;

import java.io.InputStream;
import java.util.UUID;

public record UploadSessionChunkInput(
        UUID fileId,
        UUID sessionId,
        UploadSessionChunkInput.Chunk chunk) {

    public record Chunk(InputStream data, Integer index) {

    }

}
