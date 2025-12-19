package com.arqivame.storage.infrastructure.file.persistence;

import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.Checksum.Algorithm;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSession;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity(name = "File")
@Table(name = "files")
public class FileJpaEntity {

    @Id
    private UUID id;

    @Column(name = "checksum_value")
    private String checksumValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "checksum_algorithm")
    private Checksum.Algorithm checksumAlgorithm;

    @Transient
    private Queue<Event<?>> events;

    private FileJpaEntity(
            final UUID id,
            final String checksumValue,
            final Algorithm checksumAlgorithm,
            final Queue<Event<?>> events) {
        this.id = id;
        this.checksumValue = checksumValue;
        this.checksumAlgorithm = checksumAlgorithm;
        this.events = events;
    }

    public File toDomain(final Optional<UploadSession> uploadSession) {
        return null;
        // return File.with(
        //         FileID.of(id),
        //         null,
        //         // Checksum.with(checksumValue, checksumAlgorithm),
        //         uploadSession,
        //         events);
    }

    public static FileJpaEntity fromDomain(final File file) {
        return new FileJpaEntity(
                file.getId().getValue(),
                file.getChecksum().value(),
                file.getChecksum().algorithm(),
                file.getEvents());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getChecksumValue() {
        return checksumValue;
    }

    public void setChecksumValue(String checksumValue) {
        this.checksumValue = checksumValue;
    }

    public Checksum.Algorithm getChecksumAlgorithm() {
        return checksumAlgorithm;
    }

    public void setChecksumAlgorithm(Checksum.Algorithm checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
    }

    public Queue<Event<?>> getEvents() {
        return events;
    }

    public void setEvents(Queue<Event<?>> events) {
        this.events = events;
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
        FileJpaEntity other = (FileJpaEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FileJpaEntity "
                + "[id=" + id
                + ", checksumValue=" + checksumValue
                + ", checksumAlgorithm=" + checksumAlgorithm
                + "]";
    }

}
