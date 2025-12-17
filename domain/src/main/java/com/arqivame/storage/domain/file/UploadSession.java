package com.arqivame.storage.domain.file;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class UploadSession extends Entity<UploadSessionID> {

    private final Instant createdAt;
    private final Duration maxIdleTime;
    private final Integer totalChunks;
    private final Set<Chunk> uploadedChunks;

    private UploadSession(
            final UploadSessionID id,
            final Instant createdAt,
            final Duration maxIdleTime,
            final Integer totalChunks,
            final Set<Chunk> uploadedChunks) {
        super(id);
        this.createdAt = createdAt;
        this.maxIdleTime = maxIdleTime;
        this.totalChunks = totalChunks;
        this.uploadedChunks = Objects.isNull(uploadedChunks) ? new HashSet<>() : new HashSet<>(uploadedChunks);
    }

    public static UploadSession create(final Integer totalChunks, final Duration maxIdleTime) {
        return new UploadSession(
                UploadSessionID.unique(),
                Instant.now(),
                maxIdleTime,
                totalChunks,
                Set.of());
    }

    public static UploadSession with(
            final UploadSessionID id,
            final Instant createdAt,
            final Duration maxIdleTime,
            final Integer totalChunks,
            final Set<Chunk> uploadedChunks) {
        return new UploadSession(
                id,
                createdAt,
                maxIdleTime,
                totalChunks,
                uploadedChunks);
    }

    @Override
    public void validate(ValidationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    public UploadSession addCompletedChunk(final Chunk chunk) {
        uploadedChunks.add(chunk);
        return this;
    }

    public Boolean isComplete() {
        return this.uploadedChunks.size() >= this.totalChunks;
    }

    public Boolean isIdleTimeExceeded() {
        final Instant now = Instant.now();

        final Instant lastActivity = this.uploadedChunks.stream()
                .map(Chunk::getUploadedAt)
                .max(Instant::compareTo)
                .orElse(this.createdAt);

        final Duration idleTime = Duration.between(lastActivity, now);

        return idleTime.compareTo(maxIdleTime) > 0;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public Integer getTotalChunks() {
        return totalChunks;
    }

    public Set<Chunk> getUploadedChunks() {
        return Set.copyOf(uploadedChunks);
    }

}
