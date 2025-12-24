package com.arqivame.storage.application.file.session.chunk.write;

import java.io.InputStream;
import java.util.UUID;

import com.arqivame.storage.domain.file.Checksum;

public record WriteUploadSessionChunkInput(
        UUID fileId,
        UUID uploadSessionId,
        String checksumValue,
        Checksum.Algorithm checksumAlgorithm,
        InputStream chunkData,
        Long chunkIndex) {

}
