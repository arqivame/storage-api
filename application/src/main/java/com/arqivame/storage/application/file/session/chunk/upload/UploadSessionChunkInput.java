package com.arqivame.storage.application.file.session.chunk.upload;

import java.io.InputStream;
import java.util.UUID;

import com.arqivame.storage.domain.file.Checksum;

public record UploadSessionChunkInput(
        UUID fileId,
        UUID sessionId,
        UploadSessionChunkInput.Chunk chunk) {

    public record Chunk(
            String checksumValue,
            Checksum.Algorithm algorithm,
            InputStream data,
            Integer index) {

    }

}
