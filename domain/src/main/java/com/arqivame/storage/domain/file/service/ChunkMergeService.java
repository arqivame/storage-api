package com.arqivame.storage.domain.file.service;

import java.util.Set;

import com.arqivame.storage.domain.exception.DomainException.Error;
import com.arqivame.storage.domain.exception.InvalidStateException;
import com.arqivame.storage.domain.file.Chunk;

@FunctionalInterface
public interface ChunkMergeService {

    void mergeChunks(StorageKey finalFileKey, Set<ChunkInfo> chunks);

    public record ChunkInfo(StorageKey key, Long index) {

        public static ChunkInfo with(final Chunk chunk) {
            return new ChunkInfo(
                    chunk.getStorageKey()
                            .orElseThrow(
                                    () -> InvalidStateException.with(Chunk.class,
                                            Error.with("Storage key is not set"))),
                    chunk.getIndex());
        }

    }

}
