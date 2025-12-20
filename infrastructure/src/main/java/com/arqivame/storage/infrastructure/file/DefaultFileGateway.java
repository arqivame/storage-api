package com.arqivame.storage.infrastructure.file;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSession;
import com.arqivame.storage.domain.file.UploadSessionID;
import com.arqivame.storage.infrastructure.file.persistence.ChunkJpaEntity;
import com.arqivame.storage.infrastructure.file.persistence.ChunkJpaRepository;
import com.arqivame.storage.infrastructure.file.persistence.FileJpaEntity;
import com.arqivame.storage.infrastructure.file.persistence.FileJpaRepository;
import com.arqivame.storage.infrastructure.file.persistence.UploadSessionJpaEntity;
import com.arqivame.storage.infrastructure.file.persistence.UploadSessionJpaRepository;

@Component
public class DefaultFileGateway implements FileGateway {

    private final FileJpaRepository fileJpaRepository;
    private final UploadSessionJpaRepository uploadSessionJpaRepository;
    private final ChunkJpaRepository chunkJpaRepository;

    public DefaultFileGateway(
            final FileJpaRepository fileJpaRepository,
            final UploadSessionJpaRepository uploadSessionJpaRepository,
            final ChunkJpaRepository chunkJpaRepository) {
        this.fileJpaRepository = Objects.requireNonNull(fileJpaRepository);
        this.uploadSessionJpaRepository = Objects.requireNonNull(uploadSessionJpaRepository);
        this.chunkJpaRepository = Objects.requireNonNull(chunkJpaRepository);
    }

    @Override
    public Optional<File> findById(final FileID id) {
        return fileJpaRepository
                .findById(Objects.requireNonNull(id.getValue()))
                .map(fileJpa -> fileJpa.toDomain(findUploadSession(id)));
    }

    @Override
    public File save(final File file) {

        fileJpaRepository
                .save(Objects.requireNonNull(FileJpaEntity.fromDomain(file)))
                .toDomain(file.getUploadSession());

        file
                .getUploadSession()
                .ifPresent(session -> saveChunks(file, saveUploadSession(file, session)));

        return file;
    }

    private Optional<UploadSession> findUploadSession(final FileID fileId) {
        return uploadSessionJpaRepository
                .findByFileId(fileId.getValue())
                .map(session -> session.toDomain(findAllSessionChunks(UploadSessionID.of(session.getId()))));
    }

    private Set<Chunk> findAllSessionChunks(final UploadSessionID uploadSessionId) {
        return chunkJpaRepository.findAllBySessionId(uploadSessionId.getValue())
                .stream()
                .map(ChunkJpaEntity::toDomain)
                .collect(Collectors.toSet());
    }

    private UploadSession saveUploadSession(final File file, final UploadSession uploadSession) {

        return uploadSessionJpaRepository
                .save(Objects.requireNonNull(UploadSessionJpaEntity.fromDomain(file, uploadSession)))
                .toDomain(uploadSession.getChunks());

    }

    private Set<Chunk> saveChunks(final File file, final UploadSession uploadSession) {

        if (uploadSession.getChunks().isEmpty())
            return Set.of();

        final Set<ChunkJpaEntity> chunksJpa = uploadSession
                .getChunks()
                .stream()
                .map(chunk -> ChunkJpaEntity.fromDomain(chunk, uploadSession, file))
                .collect(Collectors.toSet());

        chunkJpaRepository.saveAll(Objects.requireNonNull(chunksJpa));

        return uploadSession.getChunks();

    }

}
