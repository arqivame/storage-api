package com.arqivame.storage.infrastructure.file.persistence;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSession;
import com.arqivame.storage.domain.file.UploadSessionID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity(name = "UploadSession")
@Table(name = "upload_sessions")
public class UploadSessionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "max_idle_time", updatable = false, nullable = false)
    private Duration maxIdleTime;

    @Column(name = "max_bytes_per_second_transfer_rate_per_chunk", updatable = false, nullable = false)
    private Long maxBytesPerSecondTransferRatePerChunk;

    @Column(name = "max_chunks_at_same_time", updatable = false, nullable = false)
    private Integer maxChunksAtSameTime;

    @Column(name = "total_chunks", nullable = false)
    private Long totalChunks;

    @Column(name = "chunk_size", nullable = false)
    private Long chunkSize;

    @Column(name = "last_chunk_size", nullable = false)
    private Long lastChunkSize;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private FileJpaEntity file;

    public UploadSessionJpaEntity() {
    }

    private UploadSessionJpaEntity(
            final UUID id,
            final Instant createdAt,
            final Duration maxIdleTime,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime,
            final Long totalChunks,
            final Long chunkSize,
            final Long lastChunkSize,
            final FileJpaEntity file) {
        this.id = id;
        this.createdAt = createdAt;
        this.maxIdleTime = maxIdleTime;
        this.maxBytesPerSecondTransferRatePerChunk = maxBytesPerSecondTransferRatePerChunk;
        this.maxChunksAtSameTime = maxChunksAtSameTime;
        this.totalChunks = totalChunks;
        this.chunkSize = chunkSize;
        this.lastChunkSize = lastChunkSize;
        this.file = file;
    }

    public UploadSession toDomain(final Set<Chunk> uploadedChunks) {
        return UploadSession.with(
                UploadSessionID.of(this.id),
                FileID.of(file.getId()),
                createdAt,
                maxIdleTime,
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime,
                totalChunks,
                chunkSize,
                lastChunkSize,
                uploadedChunks);
    }

    public static UploadSessionJpaEntity fromDomain(final File file, final UploadSession session) {
        return new UploadSessionJpaEntity(
                session.getId().getValue(),
                session.getCreatedAt(),
                session.getMaxIdleTime(),
                session.getMaxBytesPerSecondTransferRatePerChunk(),
                session.getMaxChunksAtSameTime(),
                session.getTotalChunks(),
                session.getChunkSize(),
                session.getLastChunkSize(),
                FileJpaEntity.fromDomain(file));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(Duration maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public Long getMaxBytesPerSecondTransferRatePerChunk() {
        return maxBytesPerSecondTransferRatePerChunk;
    }

    public void setMaxBytesPerSecondTransferRatePerChunk(Long maxBytesPerSecondTransferRatePerChunk) {
        this.maxBytesPerSecondTransferRatePerChunk = maxBytesPerSecondTransferRatePerChunk;
    }

    public Integer getMaxChunksAtSameTime() {
        return maxChunksAtSameTime;
    }

    public void setMaxChunksAtSameTime(Integer maxChunksAtSameTime) {
        this.maxChunksAtSameTime = maxChunksAtSameTime;
    }

    public Long getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(Long totalChunks) {
        this.totalChunks = totalChunks;
    }

    public Long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Long getLastChunkSize() {
        return lastChunkSize;
    }

    public void setLastChunkSize(Long lastChunkSize) {
        this.lastChunkSize = lastChunkSize;
    }

    public FileJpaEntity getFile() {
        return file;
    }

    public void setFile(FileJpaEntity file) {
        this.file = file;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UploadSessionJpaEntity other = (UploadSessionJpaEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UploadSessionJpaEntity [id=" + id
                + ", createdAt=" + createdAt
                + ", maxIdleTime=" + maxIdleTime
                + ", maxBytesPerSecondTransferRatePerChunk=" + maxBytesPerSecondTransferRatePerChunk
                + ", maxChunksAtSameTime=" + maxChunksAtSameTime
                + ", totalChunks=" + totalChunks
                + ", chunkSize=" + chunkSize
                + ", lastChunkSize=" + lastChunkSize
                + ", file=" + file
                + "]";
    }

}