package com.arqivame.storage.infrastructure.file.persistence;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.File;
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

    @Column(name = "max_idle_time", nullable = false)
    private Duration maxIdleTime;

    @Column(name = "total_chunks", nullable = false)
    private Integer totalChunks;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private FileJpaEntity file;

    private UploadSessionJpaEntity(
            final UUID id,
            final Instant createdAt,
            final Duration maxIdleTime,
            final Integer totalChunks,
            final FileJpaEntity file) {
        this.id = id;
        this.createdAt = createdAt;
        this.maxIdleTime = maxIdleTime;
        this.totalChunks = totalChunks;
        this.file = file;
    }

    public UploadSession toDomain(final Set<Chunk> uploadedChunks) {
        return UploadSession.with(
                UploadSessionID.of(this.id),
                this.createdAt,
                this.maxIdleTime,
                this.totalChunks,
                uploadedChunks);
    }

    public static UploadSessionJpaEntity fromDomain(final File file, final UploadSession session) {
        return new UploadSessionJpaEntity(
                session.getId().getValue(),
                session.getCreatedAt(),
                session.getMaxIdleTime(),
                session.getTotalChunks(),
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

    public Integer getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(Integer totalChunks) {
        this.totalChunks = totalChunks;
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
                + ", totalChunks=" + totalChunks
                + ", file=" + file
                + "]";
    }
}