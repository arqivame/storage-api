package com.arqivame.storage.infrastructure.file.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadSessionJpaRepository extends JpaRepository<UploadSessionJpaEntity, UUID> {

    Optional<UploadSessionJpaEntity> findByFileId(UUID fileId);

}
