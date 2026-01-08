package com.arqivame.storage.infrastructure.file;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.infrastructure.file.persistence.ChunkJpaEntity;
import com.arqivame.storage.infrastructure.file.persistence.ChunkJpaRepository;
import com.arqivame.storage.infrastructure.file.persistence.FileJpaEntity;
import com.arqivame.storage.infrastructure.file.persistence.FileJpaRepository;

@Component
public class DefaultFileGateway implements FileGateway {

    private final FileJpaRepository fileJpaRepository;
    private final ChunkJpaRepository chunkJpaRepository;

    public DefaultFileGateway(
            final FileJpaRepository fileJpaRepository,
            final ChunkJpaRepository chunkJpaRepository) {
        this.fileJpaRepository = Objects.requireNonNull(fileJpaRepository);
        this.chunkJpaRepository = Objects.requireNonNull(chunkJpaRepository);
    }

    @Override
    public Optional<File> findById(final FileID id) {
        return fileJpaRepository
                .findById(Objects.requireNonNull(id.getValue()))
                .map(fileJpa -> fileJpa.toDomain(findUploadedChunks(id)));
    }

    @Transactional
    @Override
    public File create(final File file) {

        if (fileJpaRepository.existsById(Objects.requireNonNull(file.getId().getValue())))
            throw new RuntimeException("File already exists: " + file.getId().getValue());

        return save(file);
    }

    @Transactional
    @Override
    public File update(final File file) {

        if (fileJpaRepository.existsById(Objects.requireNonNull(file.getId().getValue())))
            return save(file);

        throw new RuntimeException("File not found: " + file.getId().getValue());
    }

    private File save(final File file) {

        final FileJpaEntity fileJpaEntity = fileJpaRepository
                .saveAndFlush(Objects.requireNonNull(FileJpaEntity.fromDomain(file)));

        saveChunks(file.getUploadedChunks(), fileJpaEntity);

        return file;
    }

    private Set<Chunk> findUploadedChunks(final FileID fileId) {
        return chunkJpaRepository
                .findAllByFileId(fileId.getValue())
                .stream()
                .map(ChunkJpaEntity::toDomain)
                .collect(Collectors.toSet());
    }

    private Set<Chunk> saveChunks(final Set<Chunk> chunks, final FileJpaEntity fileJpaEntity) {

        final List<ChunkJpaEntity> chunksJpa = chunks
                .stream()
                .map(chunk -> ChunkJpaEntity.fromDomain(chunk, fileJpaEntity))
                .toList();

        chunkJpaRepository.saveAllAndFlush(Objects.requireNonNull(chunksJpa));

        return chunks;

    }

}
