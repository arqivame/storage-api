package com.arqivame.storage.infrastructure.file.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<FileJpaEntity, UUID> {

}
