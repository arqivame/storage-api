package com.arqivame.storage.infrastructure.file.service;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.arqivame.storage.domain.file.service.ChunkMergeService;
import com.arqivame.storage.domain.file.service.StorageKey;
import com.arqivame.storage.infrastructure.commons.FileSystemUtils;
import com.arqivame.storage.infrastructure.commons.SequentialIterator;

public class FileSystemChunkMergeService implements ChunkMergeService {

    private final Path rootLocation;

    public FileSystemChunkMergeService(final Path rootLocation) {
        this.rootLocation = Objects.requireNonNull(rootLocation);
    }

    @Override
    public MergeResult mergeChunks(final StorageKey finalFileKey, final Set<ChunkInfo> chunks) {

        final Path finalFilePath = toPath(finalFileKey);

        final Set<SequentialIterator.Item<Path>> items = chunks
                .stream()
                .map(chunk -> SequentialIterator.Item.of(toPath(chunk.key()), chunk.index()))
                .collect(Collectors.toSet());

        final SequentialIterator<Path> iterator = SequentialIterator.of(items);

        try {
            FileSystemUtils.append(finalFilePath, iterator);
        } catch (Exception e) {
            final Set<ChunkInfo> nonMergedChunks = new HashSet<>();
            iterator.forEachRemaining(item -> nonMergedChunks.add(findChunkInfo(chunks, fromPath(item))));
            MergeResult.partialMerge(nonMergedChunks);
        }

        return MergeResult.allMerged();
    }

    private static ChunkInfo findChunkInfo(final Set<ChunkInfo> chunkInfos, final StorageKey storageKey) {
        return chunkInfos
                .stream()
                .filter(chunkInfo -> chunkInfo.key().equals(storageKey))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Chunk with index "
                                + storageKey
                                + " not found in the provided chunks set."));
    }

    private StorageKey fromPath(final Path path) {
        return StorageKey.of(rootLocation.relativize(path).toString());
    }

    private Path toPath(final StorageKey storageKey) {
        return rootLocation.resolve(storageKey.getFullKey());
    }

}
