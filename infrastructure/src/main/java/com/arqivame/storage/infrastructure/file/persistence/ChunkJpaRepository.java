package com.arqivame.storage.infrastructure.file.persistence;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChunkJpaRepository extends JpaRepository<ChunkJpaEntity, UUID> {

    Set<ChunkJpaEntity> findAllBySessionId(UUID sessionId);

}
