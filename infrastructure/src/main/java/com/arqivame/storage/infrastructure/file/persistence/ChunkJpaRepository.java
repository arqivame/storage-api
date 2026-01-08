package com.arqivame.storage.infrastructure.file.persistence;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChunkJpaRepository extends JpaRepository<ChunkJpaEntity, ChunkJpaID> {

    Set<ChunkJpaEntity> findAllByFileId(UUID fileId);

}
