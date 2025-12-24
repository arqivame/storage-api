package com.arqivame.storage.infrastructure.file.persistence;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadSessionJpaRepository extends JpaRepository<UploadSessionJpaEntity, UUID> {

    Set<UploadSessionJpaEntity> findAllByFileId(UUID fileId);

}
