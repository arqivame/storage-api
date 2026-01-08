package com.arqivame.storage.infrastructure.file.persistence;

import com.arqivame.storage.domain.file.Chunk;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "Chunk")
@Table(name = "chunks")
public class ChunkJpaEntity {

    @EmbeddedId
    private ChunkJpaID id;

    @Column(name = "index", nullable = false, updatable = false, insertable = false)
    private Long index;

    @Column(name = "size", nullable = false)
    private Long size;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", updatable = false, insertable = false)
    private FileJpaEntity file;

    public ChunkJpaEntity() {
    }

    public ChunkJpaEntity(
            final ChunkJpaID id,
            final Long index,
            final Long size,
            final FileJpaEntity file) {
        this.id = id;
        this.index = index;
        this.size = size;
        this.file = file;
    }

    public static ChunkJpaEntity fromDomain(
            final Chunk chunk,
            final FileJpaEntity fileJpaEntity) {
        return new ChunkJpaEntity(
                new ChunkJpaID(fileJpaEntity.getId(), chunk.index()),
                chunk.index(),
                chunk.size(),
                fileJpaEntity);
    }

    public Chunk toDomain() {
        return new Chunk(index, size);
    }

    public ChunkJpaID getId() {
        return id;
    }

    public void setId(ChunkJpaID id) {
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
                + ", file=" + file
                + "]";
    }

}
