package com.arqivame.storage.infrastructure.file.persistence;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.ChunkID;
import com.arqivame.storage.domain.file.ChunkStatus;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSession;
import com.arqivame.storage.domain.file.service.StorageKey;
import com.arqivame.storage.domain.file.service.StorageWriter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity(name = "Chunk")
@Table(name = "chunks")
public class ChunkJpaEntity {

    @Id
    private UUID id;

    @Column(name = "chunk_index", nullable = false)
    private Long index;

    @Column(name = "chunk_size", nullable = false)
    private Long size;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChunkStatus status;

    @Column(name = "storage_key")
    private String storageKey;

    @Column(name = "written_at")
    private Instant writtenAt;

    @Transient
    private Optional<StorageWriter> writer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", updatable = false)
    private UploadSessionJpaEntity session;

    public ChunkJpaEntity() {
    }

    private ChunkJpaEntity(
            final UUID id,
            final Long index,
            final Long size,
            final ChunkStatus status,
            final String storageKey,
            final Instant writtenAt,
            final Optional<StorageWriter> writer,
            final UploadSessionJpaEntity session) {
        this.id = id;
        this.index = index;
        this.size = size;
        this.status = status;
        this.storageKey = storageKey;
        this.writtenAt = writtenAt;
        this.writer = writer;
        this.session = session;
    }

    public static ChunkJpaEntity fromDomain(
            final Chunk chunk,
            final UploadSession session,
            final File file) {
        return new ChunkJpaEntity(
                chunk.getId().getValue(),
                chunk.getIndex(),
                chunk.getSize(),
                chunk.getStatus(),
                chunk.getStorageKey().map(StorageKey::getFullKey).orElse(null),
                chunk.getWrittenAt(),
                chunk.getWriter(),
                UploadSessionJpaEntity.fromDomain(file, session));
    }

    public Chunk toDomain() {
        return Chunk.with(
                ChunkID.of(id),
                index,
                size,
                status,
                Objects.isNull(storageKey) ? null : StorageKey.of(storageKey),
                writtenAt,
                Objects.isNull(writer) ? null : writer.orElse(null));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public ChunkStatus getStatus() {
        return status;
    }

    public void setStatus(ChunkStatus status) {
        this.status = status;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public Instant getWrittenAt() {
        return writtenAt;
    }

    public void setWrittenAt(Instant writtenAt) {
        this.writtenAt = writtenAt;
    }

    public Optional<StorageWriter> getWriter() {
        return writer;
    }

    public void setWriter(Optional<StorageWriter> writer) {
        this.writer = writer;
    }

    public UploadSessionJpaEntity getSession() {
        return session;
    }

    public void setSession(UploadSessionJpaEntity session) {
        this.session = session;
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
        ChunkJpaEntity other = (ChunkJpaEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ChunkJpaEntity [id=" + id
                + ", index=" + index
                + ", size=" + size
                + ", status=" + status
                + ", storageKey=" + storageKey
                + ", writtenAt=" + writtenAt
                + ", session=" + session
                + "]";
    }

}
