package com.arqivame.storage.infrastructure.file.service;

import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

        final Set<SequentialIterator.Item<ChunkInfo>> items = chunks
                .stream()
                .map(chunk -> SequentialIterator.Item.of(chunk, chunk.index()))
                .collect(Collectors.toSet());

        final SequentialIterator<ChunkInfo> iterator = SequentialIterator.of(items);

        try (final FileChannel outputChannel = FileSystemUtils.opeChannel(finalFilePath, StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {

            while (iterator.hasNext()) {
                try (final FileChannel inputChannel = FileSystemUtils.opeChannel(
                        toPath(iterator.next().key()),
                        StandardOpenOption.READ)) {
                    FileSystemUtils.append(outputChannel.position(), outputChannel, inputChannel);
                }
            }

            // TODO notificar que chunk foi mesclado aqui??

        } catch (Exception e) {
            final Set<ChunkInfo> nonMergedChunks = new HashSet<>();
            iterator.forEachRemaining(item -> nonMergedChunks.add(item));
            MergeResult.partialMerge(nonMergedChunks);
        }

        return MergeResult.allMerged();
    }

    private Path toPath(final StorageKey storageKey) {
        return rootLocation.resolve(storageKey.getFullKey());
    }

}
