package com.arqivame.storage.application.usecase.file.chunk.upload;

import java.io.InputStream;
import java.util.UUID;

import com.arqivame.storage.domain.file.Checksum;

public record UploadChunkInput(
        UUID fileId,
        String checksumValue,
        Checksum.Algorithm checksumAlgorithm,
        InputStream chunkData,
        Long chunkIndex) {

}
