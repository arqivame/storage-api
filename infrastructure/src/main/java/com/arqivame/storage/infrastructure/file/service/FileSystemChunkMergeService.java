package com.arqivame.storage.infrastructure.file.service;

import java.nio.file.Path;
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
    public void mergeChunks(final StorageKey finalFileKey, final Set<ChunkInfo> chunks) {

        final Path finalFilePath = toPath(rootLocation, finalFileKey);

        final Set<SequentialIterator.Item<Path>> items = chunks
                .stream()
                .map(chunk -> SequentialIterator.Item.of(toPath(rootLocation, chunk.key()), chunk.index()))
                .collect(Collectors.toSet());

        final SequentialIterator<Path> iterator = SequentialIterator.of(items);

        FileSystemUtils.append(finalFilePath, iterator);

    }

    private static Path toPath(final Path rootLocation, final StorageKey storageKey) {
        // TODO ver se mantem a lógica de lastSegment
        final String fullKey = storageKey.getFullKey();
        final String lastSegment = storageKey.lastSegment();
        return rootLocation.resolve(fullKey).resolve(lastSegment);
    }

}
