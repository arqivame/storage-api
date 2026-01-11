package com.arqivame.storage.application.port;

import java.io.InputStream;

import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.FileID;

@FunctionalInterface
public interface ChunkWriter {

    Checksum writeChunk(
            FileID key,
            Long chunkIndex,
            InputStream inputStream,
            Long sizeInBytes,
            Long bytesPerSecondsWrittenRate,
            Checksum.Algorithm checksumAlgorithm);

}
