package com.arqivame.storage.infrastructure.file.persistence;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class ChunkJpaID implements Serializable {

    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Column(name = "chunk_index", nullable = false)
    private Integer index;

    private ChunkJpaID(
            final UUID fileId,
            final UUID sessionId,
            final Integer index) {
        this.fileId = fileId;
        this.sessionId = sessionId;
        this.index = index;
    }

    public static ChunkJpaID from(final UUID fileId, final UUID sessionId, final Integer index) {
        return new ChunkJpaID(fileId, sessionId, index);
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
        result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
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
        if (sessionId == null) {
            if (other.sessionId != null)
                return false;
        } else if (!sessionId.equals(other.sessionId))
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
        return "ChunkJpaID [fileId=" + fileId + ", sessionId=" + sessionId + ", index=" + index + "]";
    }

}
