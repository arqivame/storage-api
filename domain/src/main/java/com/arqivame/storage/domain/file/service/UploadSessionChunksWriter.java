package com.arqivame.storage.domain.file.service;

import java.util.Set;

import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.UploadSessionID;

@FunctionalInterface
public interface UploadSessionChunksWriter {

    void write(UploadSessionID uploadSessionId, Set<Chunk> chunks);

}
