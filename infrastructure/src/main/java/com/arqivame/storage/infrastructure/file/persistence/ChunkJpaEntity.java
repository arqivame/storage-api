package com.arqivame.storage.infrastructure.file.persistence;

import java.io.InputStream;
import java.time.Instant;

import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSession;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity(name = "Chunk")
@Table(name = "chunks")
public class ChunkJpaEntity {

    @EmbeddedId
    private ChunkJpaID id;

    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    @Transient
    private InputStream writableStream;

    private ChunkJpaEntity(
            final ChunkJpaID id,
            final Instant uploadedAt,
            final InputStream writableStream) {
        this.id = id;
        this.uploadedAt = uploadedAt;
        this.writableStream = writableStream;
    }

    public static ChunkJpaEntity fromDomain(
            final File file,
            final UploadSession session,
            final Chunk chunk) {

        return new ChunkJpaEntity(
                ChunkJpaID.from(
                        file.getId().getValue(),
                        session.getId().getValue(),
                        chunk.getIndex()),
                chunk.getUploadedAt(),
                chunk.getWritableStream().orElse(null));

    }

    public Chunk toDomain() {
        return Chunk.from(
                this.id.getIndex(),
                this.uploadedAt,
                this.writableStream);
    }

    public ChunkJpaID getId() {
        return id;
    }

    public void setId(ChunkJpaID id) {
        this.id = id;
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
        return "ChunkJpaEntity [id=" + id + ", uploadedAt=" + uploadedAt + "]";
    }

}
