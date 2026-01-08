package com.arqivame.storage.infrastructure.file.persistence;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ChunkJpaID implements Serializable {

    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @Column(name = "index", nullable = false)
    private Long index;

    public ChunkJpaID() {
    }

    public ChunkJpaID(final UUID fileId, final Long index) {
        this.fileId = fileId;
        this.index = index;
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
        result = prime * result + ((index == null) ? 0 : index.hashCode());
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
        ChunkJpaID other = (ChunkJpaID) obj;
        if (fileId == null) {
            if (other.fileId != null)
                return false;
        } else if (!fileId.equals(other.fileId))
            return false;
        if (index == null) {
            if (other.index != null)
                return false;
        } else if (!index.equals(other.index))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ChunkJpaID [fileId=" + fileId
                + ", index=" + index
                + "]";
    }

}
