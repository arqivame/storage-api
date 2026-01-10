package com.arqivame.storage.infrastructure.file.service.filesystem;

import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.arqivame.storage.infrastructure.commons.FileSystemUtils;
import com.arqivame.storage.infrastructure.commons.SequentialIterator;
import com.arqivame.storage.infrastructure.file.service.FileAssembler;
import com.arqivame.storage.infrastructure.storage.service.StorageKey;

public class FileSystemFileAssembler implements FileAssembler {

    private final Path rootLocation;

    public FileSystemFileAssembler(final Path rootLocation) {
        this.rootLocation = Objects.requireNonNull(rootLocation);
    }

    @Override
    public MergeResult mergeChunks(
            final StorageKey finalFileKey,
            final Set<ChunkInfo> chunks,
            final Long firstChunkSize) {

        final Set<SequentialIterator.Item<ChunkInfo>> items = chunks
                .stream()
                .map(chunk -> SequentialIterator.Item.of(chunk, chunk.index()))
                .collect(Collectors.toSet());

        final SequentialIterator<ChunkInfo> iterator = SequentialIterator.of(items);

        try (final FileChannel outputChannel = FileSystemUtils.opeChannel(
                toPath(finalFileKey),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {

            while (iterator.hasNext()) {

                final ChunkInfo chunkInfo = iterator.next();

                if (!FileSystemUtils.exists(toPath(chunkInfo.key())))
                    continue;

                try (final FileChannel inputChannel = FileSystemUtils.opeChannel(
                        toPath(chunkInfo.key()),
                        StandardOpenOption.READ)) {

                    final Long offset = (chunkInfo.index() * firstChunkSize);
                    FileSystemUtils.append(offset, outputChannel, inputChannel);

                }

                FileSystemUtils.delete(toPath(chunkInfo.key()));
            }

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
