package com.arqivame.storage.infrastructure.file;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.infrastructure.file.persistence.FileJpaEntity;
import com.arqivame.storage.infrastructure.file.persistence.FileJpaRepository;

@Component
public class DefaultFileGateway implements FileGateway {

    private final FileJpaRepository fileJpaRepository;

    public DefaultFileGateway(final FileJpaRepository fileJpaRepository) {
        this.fileJpaRepository = Objects.requireNonNull(fileJpaRepository);
    }

    @Override
    public Optional<File> findById(final FileID id) {
        return fileJpaRepository
                .findById(Objects.requireNonNull(id.getValue()))
                .map(fileJpa -> fileJpa.toDomain());
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

        fileJpaRepository.saveAndFlush(Objects.requireNonNull(FileJpaEntity.fromDomain(file)));

        return file;
    }

}
