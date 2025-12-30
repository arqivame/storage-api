package com.arqivame.storage.domain.file.service;

import java.util.Objects;
import java.util.Set;

import com.arqivame.storage.domain.exception.DomainException.Error;
import com.arqivame.storage.domain.exception.InvalidStateException;
import com.arqivame.storage.domain.file.Chunk;

@FunctionalInterface
public interface ChunkMergeService {

    MergeResult mergeChunks(StorageKey finalFileKey, Set<ChunkInfo> chunks, Long firstChunkSize, Long lastChunkSize);

    public record ChunkInfo(StorageKey key, Long size, Long index) {

        public static ChunkInfo with(final Chunk chunk) {
            return new ChunkInfo(
                    chunk.getStorageKey()
                            .orElseThrow(
                                    () -> InvalidStateException.with(Chunk.class,
                                            Error.with("Storage key is not set"))),
                    chunk.getSize(),
                    chunk.getIndex());
        }

    }

    public record MergeResult(Boolean allChunksMerged, Set<ChunkInfo> nonMergedChunks) {

        private MergeResult(final Set<ChunkInfo> nonMergedChunks) {
            this(nonMergedChunks.isEmpty(), Set.copyOf(nonMergedChunks));
        }

        public static MergeResult allMerged() {
            return new MergeResult(Set.of());
        }

        public static MergeResult partialMerge(final Set<ChunkInfo> nonMergedChunks) {
            return new MergeResult(Objects.requireNonNullElse(nonMergedChunks, Set.of()));
        }

    }

}
