package com.arqivame.storage.application.service.storage;

import java.util.Objects;
import java.util.Set;

@FunctionalInterface
public interface FileAssemblerService {

    MergeResult mergeChunks(StorageKey finalFileKey, Set<ChunkInfo> chunks, Long firstChunkSize);

    public record ChunkInfo(StorageKey key, Long index) {

        public static ChunkInfo with(final StorageKey chunkStorageKey, final Long index) {
            return new ChunkInfo(chunkStorageKey, index);
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
