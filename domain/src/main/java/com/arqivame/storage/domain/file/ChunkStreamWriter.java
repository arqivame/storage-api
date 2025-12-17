package com.arqivame.storage.domain.file;

import java.io.InputStream;

@FunctionalInterface
public interface ChunkStreamWriter {

    void write(InputStream chunkStream);

}
