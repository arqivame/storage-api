package com.arqivame.storage.application.port;

import java.io.InputStream;

import com.arqivame.storage.domain.file.FileID;

@FunctionalInterface
public interface ChunkReader {

    InputStream readChunk(
            FileID key,
            Long offset,
            Long sizeInBytes,
            Long bytesPerSecondsRate);

}
