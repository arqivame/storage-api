package com.arqivame.storage.infrastructure.file.persistence;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.arqivame.storage.domain.file.Checksum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "UploadSession")
@Table(name = "upload_sessions")
public class FileJpaEntity {

    @Id
    private UUID id;

    @Column(name = "checksum_value", nullable = false)
    private String checksumValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "checksum_algorithm", nullable = false)
    private Checksum.Algorithm checksumAlgorithm;

    @Column(name = "upload_session_created_at", nullable = false)
    private Instant uploadSessionCreatedAt;

    @Column(name = "upload_session_max_idle_time", nullable = false)
    private Duration uploadSessionMaxIdleTime;

    @Column(name = "upload_session_total_chunks", nullable = false)
    private Integer uploadSessionTotalChunks;

    @Column(name = "upload_session_uploaded_chunks", nullable = false)
    private Set<ChunkJpaEntity> uploadSessionUploadedChunks;

}
