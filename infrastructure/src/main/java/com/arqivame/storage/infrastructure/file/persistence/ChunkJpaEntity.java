package com.arqivame.storage.infrastructure.file.persistence;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.ChunkID;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSession;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private Integer index;

    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    @Transient
    private InputStream writableStream;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", insertable = false, updatable = false)
    private UploadSessionJpaEntity session;

    private ChunkJpaEntity(
            final UUID id,
            final Instant uploadedAt,
            final InputStream writableStream,
            final UploadSessionJpaEntity session) {
        this.id = id;
        this.uploadedAt = uploadedAt;
        this.writableStream = writableStream;
        this.session = session;
    }

    public static ChunkJpaEntity fromDomain(
            final Chunk chunk,
            final UploadSession session,
            final File file) {

        return new ChunkJpaEntity(
                chunk.getId().getValue(),
                chunk.getUploadedAt(),
                chunk.getWritableStream().orElse(null),
                UploadSessionJpaEntity.fromDomain(file, session));
    }

    public Chunk toDomain() {

        return null;
        // return Chunk.from(
        //         ChunkID.of(this.id),
        //         this.index,
        //         this.uploadedAt,
        //         this.writableStream);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public InputStream getWritableStream() {
        return writableStream;
    }

    public void setWritableStream(InputStream writableStream) {
        this.writableStream = writableStream;
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
                + ", uploadedAt=" + uploadedAt
                + ", writableStream=" + writableStream
                + ", session=" + session
                + "]";
    }

}
