package com.arqivame.storage.infrastructure.file.persistence;

import java.time.Instant;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.Checksum.Algorithm;
import com.arqivame.storage.domain.file.Chunk;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.FileStatus;
import com.arqivame.storage.domain.file.Session;
import com.arqivame.storage.domain.file.service.StorageKey;

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

    @Column(name = "full_storage_key", updatable = false, nullable = false)
    private String fullStorageKey;

    @Column(name = "checksum_value", nullable = false) // updatable = false, nullable = false
    private String checksumValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "checksum_algorithm", nullable = false) // updatable = false, nullable = false
    private Checksum.Algorithm checksumAlgorithm;

    @Column(name = "size", updatable = false, nullable = false)
    private Long size;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FileStatus status;

    @Column(name = "up_session_created_at")
    private Instant uploadSessionCreatedAt;

    @Column(name = "up_session_max_chunk_transfer_rate_bps")
    private Long uploadSessionChunkMaxRateBps;

    @Column(name = "up_session_max_chunks_at_same_time")
    private Integer uploadSessionMaxChunksAtSameTime;

    @Column(name = "up_session_total_chunks")
    private Integer uploadSessionTotalChunks;

    @Column(name = "up_session_chunk_size")
    private Long uploadSessionChunkSize;

    @Column(name = "up_session_last_chunk_size")
    private Long uploadSessionLastChunkSize;

    @Column(name = "down_session_created_at")
    private Instant downloadSessionCreatedAt;

    @Column(name = "down_session_max_chunk_transfer_rate_bps")
    private Long downloadChunkMaxTransferRateBps;

    @Column(name = "down_session_max_chunks_at_same_time")
    private Integer downloadSessionMaxChunksAtSameTime;

    @Column(name = "down_session_total_chunks")
    private Integer downloadSessionTotalChunks;

    @Column(name = "down_session_chunk_size")
    private Long downloadSessionChunkSize;

    @Column(name = "down_session_last_chunk_size")
    private Long downloadSessionLastChunkSize;

    @Transient
    private Queue<Event<?>> events;

    public FileJpaEntity(
            UUID id,
            String fullStorageKey,
            String checksumValue,
            Algorithm checksumAlgorithm,
            Long size,
            FileStatus status,
            Instant uploadSessionCreatedAt,
            Long uploadSessionMaxBytesPerSecondTransferRatePerChunk,
            Integer uploadSessionMaxChunksAtSameTime,
            Integer uploadSessionTotalChunks,
            Long uploadSessionChunkSize,
            Long uploadSessionLastChunkSize,
            Instant downloadSessionCreatedAt,
            Long downloadSessionMaxBytesPerSecondTransferRatePerChunk,
            Integer downloadSessionMaxChunksAtSameTime,
            Integer downloadSessionTotalChunks,
            Long downloadSessionChunkSize,
            Long downloadSessionLastChunkSize,
            Queue<Event<?>> events) {
        this.id = id;
        this.fullStorageKey = fullStorageKey;
        this.checksumValue = checksumValue;
        this.checksumAlgorithm = checksumAlgorithm;
        this.size = size;
        this.status = status;
        this.uploadSessionCreatedAt = uploadSessionCreatedAt;
        this.uploadSessionChunkMaxRateBps = uploadSessionMaxBytesPerSecondTransferRatePerChunk;
        this.uploadSessionMaxChunksAtSameTime = uploadSessionMaxChunksAtSameTime;
        this.uploadSessionTotalChunks = uploadSessionTotalChunks;
        this.uploadSessionChunkSize = uploadSessionChunkSize;
        this.uploadSessionLastChunkSize = uploadSessionLastChunkSize;
        this.downloadSessionCreatedAt = downloadSessionCreatedAt;
        this.downloadChunkMaxTransferRateBps = downloadSessionMaxBytesPerSecondTransferRatePerChunk;
        this.downloadSessionMaxChunksAtSameTime = downloadSessionMaxChunksAtSameTime;
        this.downloadSessionTotalChunks = downloadSessionTotalChunks;
        this.downloadSessionChunkSize = downloadSessionChunkSize;
        this.downloadSessionLastChunkSize = downloadSessionLastChunkSize;
        this.events = events;
    }

    public FileJpaEntity() {
    }

    public static FileJpaEntity fromDomain(final File file) {
        return new FileJpaEntity(file.getId().getValue(),
                file.getStorageKey().getFullKey(),
                file.getChecksum().value(),
                file.getChecksum().algorithm(),
                file.getSize(),
                file.getStatus(),
                file.getUploadSession().map(Session::createdAt).orElse(null),
                file.getUploadSession().map(Session::maxBytesPerSecondTransferRatePerChunk).orElse(null),
                file.getUploadSession().map(Session::maxChunksAtSameTime).orElse(null),
                file.getUploadSession().map(Session::totalChunks).orElse(null),
                file.getUploadSession().map(Session::chunkSize).orElse(null),
                file.getUploadSession().map(Session::lastChunkSize).orElse(null),
                file.getDownloadSession().map(Session::createdAt).orElse(null),
                file.getDownloadSession().map(Session::maxBytesPerSecondTransferRatePerChunk).orElse(null),
                file.getDownloadSession().map(Session::maxChunksAtSameTime).orElse(null),
                file.getDownloadSession().map(Session::totalChunks).orElse(null),
                file.getDownloadSession().map(Session::chunkSize).orElse(null),
                file.getDownloadSession().map(Session::lastChunkSize).orElse(null),
                file.getEvents());
    }

    public File toDomain(final Set<Chunk> uploadedChunks) {
        return File.with(
                FileID.of(id),
                StorageKey.of(fullStorageKey),
                Checksum.from(checksumValue, checksumAlgorithm),
                size,
                status,
                uploadSession(),
                uploadedChunks,
                downloadSession(),
                events);
    }

    private Optional<Session> uploadSession() {
        if (uploadSessionCreatedAt == null) {
            return Optional.empty();
        }
        return Optional.of(new Session(
                uploadSessionCreatedAt,
                uploadSessionChunkMaxRateBps,
                uploadSessionMaxChunksAtSameTime,
                uploadSessionTotalChunks,
                uploadSessionChunkSize,
                uploadSessionLastChunkSize));
    }

    private Optional<Session> downloadSession() {
        if (downloadSessionCreatedAt == null) {
            return Optional.empty();
        }
        return Optional.of(new Session(
                downloadSessionCreatedAt,
                downloadChunkMaxTransferRateBps,
                downloadSessionMaxChunksAtSameTime,
                downloadSessionTotalChunks,
                downloadSessionChunkSize,
                downloadSessionLastChunkSize));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullStorageKey() {
        return fullStorageKey;
    }

    public void setFullStorageKey(String fullStorageKey) {
        this.fullStorageKey = fullStorageKey;
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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public FileStatus getStatus() {
        return status;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public Instant getUploadSessionCreatedAt() {
        return uploadSessionCreatedAt;
    }

    public void setUploadSessionCreatedAt(Instant uploadSessionCreatedAt) {
        this.uploadSessionCreatedAt = uploadSessionCreatedAt;
    }

    public Long getUploadSessionChunkMaxRateBps() {
        return uploadSessionChunkMaxRateBps;
    }

    public void setUploadSessionChunkMaxRateBps(
            Long uploadSessionMaxBytesPerSecondTransferRatePerChunk) {
        this.uploadSessionChunkMaxRateBps = uploadSessionMaxBytesPerSecondTransferRatePerChunk;
    }

    public Integer getUploadSessionMaxChunksAtSameTime() {
        return uploadSessionMaxChunksAtSameTime;
    }

    public void setUploadSessionMaxChunksAtSameTime(Integer uploadSessionMaxChunksAtSameTime) {
        this.uploadSessionMaxChunksAtSameTime = uploadSessionMaxChunksAtSameTime;
    }

    public Integer getUploadSessionTotalChunks() {
        return uploadSessionTotalChunks;
    }

    public void setUploadSessionTotalChunks(Integer uploadSessionTotalChunks) {
        this.uploadSessionTotalChunks = uploadSessionTotalChunks;
    }

    public Long getUploadSessionChunkSize() {
        return uploadSessionChunkSize;
    }

    public void setUploadSessionChunkSize(Long uploadSessionChunkSize) {
        this.uploadSessionChunkSize = uploadSessionChunkSize;
    }

    public Long getUploadSessionLastChunkSize() {
        return uploadSessionLastChunkSize;
    }

    public void setUploadSessionLastChunkSize(Long uploadSessionLastChunkSize) {
        this.uploadSessionLastChunkSize = uploadSessionLastChunkSize;
    }

    public Instant getDownloadSessionCreatedAt() {
        return downloadSessionCreatedAt;
    }

    public void setDownloadSessionCreatedAt(Instant downloadSessionCreatedAt) {
        this.downloadSessionCreatedAt = downloadSessionCreatedAt;
    }

    public Long getDownloadChunkMaxTransferRateBps() {
        return downloadChunkMaxTransferRateBps;
    }

    public void setDownloadChunkMaxTransferRateBps(
            Long downloadSessionMaxBytesPerSecondTransferRatePerChunk) {
        this.downloadChunkMaxTransferRateBps = downloadSessionMaxBytesPerSecondTransferRatePerChunk;
    }

    public Integer getDownloadSessionMaxChunksAtSameTime() {
        return downloadSessionMaxChunksAtSameTime;
    }

    public void setDownloadSessionMaxChunksAtSameTime(Integer downloadSessionMaxChunksAtSameTime) {
        this.downloadSessionMaxChunksAtSameTime = downloadSessionMaxChunksAtSameTime;
    }

    public Integer getDownloadSessionTotalChunks() {
        return downloadSessionTotalChunks;
    }

    public void setDownloadSessionTotalChunks(Integer downloadSessionTotalChunks) {
        this.downloadSessionTotalChunks = downloadSessionTotalChunks;
    }

    public Long getDownloadSessionChunkSize() {
        return downloadSessionChunkSize;
    }

    public void setDownloadSessionChunkSize(Long downloadSessionChunkSize) {
        this.downloadSessionChunkSize = downloadSessionChunkSize;
    }

    public Long getDownloadSessionLastChunkSize() {
        return downloadSessionLastChunkSize;
    }

    public void setDownloadSessionLastChunkSize(Long downloadSessionLastChunkSize) {
        this.downloadSessionLastChunkSize = downloadSessionLastChunkSize;
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
        return "FileJpaEntity [id=" + id
                + ", fullStorageKey=" + fullStorageKey
                + ", checksumValue=" + checksumValue
                + ", checksumAlgorithm=" + checksumAlgorithm
                + ", size=" + size
                + ", status=" + status
                + ", uploadSessionCreatedAt=" + uploadSessionCreatedAt
                + ", uploadSessionChunkMaxRateBps=" + uploadSessionChunkMaxRateBps
                + ", uploadSessionMaxChunksAtSameTime=" + uploadSessionMaxChunksAtSameTime
                + ", uploadSessionTotalChunks=" + uploadSessionTotalChunks
                + ", uploadSessionChunkSize=" + uploadSessionChunkSize
                + ", uploadSessionLastChunkSize=" + uploadSessionLastChunkSize
                + ", downloadSessionCreatedAt=" + downloadSessionCreatedAt
                + ", downloadChunkMaxTransferRateBps=" + downloadChunkMaxTransferRateBps
                + ", downloadSessionMaxChunksAtSameTime=" + downloadSessionMaxChunksAtSameTime
                + ", downloadSessionTotalChunks=" + downloadSessionTotalChunks
                + ", downloadSessionChunkSize=" + downloadSessionChunkSize
                + ", downloadSessionLastChunkSize=" + downloadSessionLastChunkSize
                + ", events=" + events
                + "]";
    }

}
