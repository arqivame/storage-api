package com.arqivame.storage.domain.file;

import java.io.InputStream;
import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import com.arqivame.storage.domain.AggregateRoot;
import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventSource;
import com.arqivame.storage.domain.exception.NotFoundException;
import com.arqivame.storage.domain.exception.UploadSessionAlreadyOpenException;
import com.arqivame.storage.domain.file.event.FileUploadSessionCanceledEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionChunksPhysicallyDeletedEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionMarkedForDeletionEvent;
import com.arqivame.storage.domain.file.service.StorageDeleter;
import com.arqivame.storage.domain.file.service.StorageWriter;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class File extends AggregateRoot<FileID> implements EventSource {

    private final Checksum checksum;
    private final Long size;
    private final Set<UploadSession> uploadSessions;

    private final Queue<Event<?>> events;

    private File(
            final FileID id,
            final Checksum checksum,
            final Long size,
            final Set<UploadSession> uploadSessions,
            final Queue<Event<?>> events) {
        super(id);
        this.checksum = checksum;
        this.size = size;
        this.uploadSessions = Objects.isNull(uploadSessions) ? new HashSet<>() : new HashSet<>(uploadSessions);

        this.events = Objects.isNull(events) ? new java.util.LinkedList<>() : new java.util.LinkedList<>(events);
    }

    public static File create(
            final FileID id,
            final Long size,
            final Checksum checksum) {
        return new File(
                id,
                checksum,
                size,
                Set.of(),
                new LinkedList<>());
    }

    public static File with(
            final FileID id,
            final Checksum checksum,
            final Long size,
            final Set<UploadSession> uploadSessions,
            final Queue<Event<?>> events) {
        return new File(
                id,
                checksum,
                size,
                uploadSessions,
                events);
    }

    @Override
    public void validate(ValidationHandler handler) {
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    public Optional<Event<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    public UploadSessionID openUploadSession(
            final Long totalChunks,
            final Long chunkSize,
            final Long lastChunkSize,
            final Duration maxIdleTime,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime) {

        final Boolean hasAnySessionActive = uploadSessions
                .stream()
                .map(UploadSession::getStatus)
                .anyMatch(status -> UploadSessionStatus.ACTIVE.equals(status));

        if (hasAnySessionActive)
            throw UploadSessionAlreadyOpenException.create();

        final UploadSession session = UploadSession.create(
                this,
                totalChunks,
                chunkSize,
                lastChunkSize,
                maxIdleTime,
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime);

        uploadSessions.add(session);

        return session.getId();

    }

    public File markUploadSessionForDeletion(final UploadSessionID sessionId) {

        final UploadSession uploadSession = fetchUploadSessionById(sessionId);

        uploadSession.markForDeletion();
        events.add(FileUploadSessionMarkedForDeletionEvent.create(this, uploadSession));

        return this;
    }

    public File physicallyDeleteUploadSession(final UploadSessionID sessionId, final StorageDeleter storageDeleter) {

        final UploadSession uploadSession = fetchUploadSessionById(sessionId);

        uploadSession.physicallyDeleteChunks(storageDeleter);

        events.add(FileUploadSessionChunksPhysicallyDeletedEvent.create(this, uploadSession));

        return this;
    }

    public File initiateChunkWriting(
            final UploadSessionID sessionId,
            final Long chunkIndex,
            final StorageWriter writer) {

        fetchUploadSessionById(sessionId).initiateChunkWriting(chunkIndex, writer);

        return this;
    }

    public File writeChunk(
            final UploadSessionID sessionId,
            final Long chunkIndex,
            final Checksum checksum,
            final InputStream chunkData) {

        fetchUploadSessionById(sessionId).writeChunk(chunkIndex, checksum, chunkData);

        return this;
    }

    public File cancelUploadSession(final UploadSessionID sessionId) {

        final UploadSession canceledSession = fetchUploadSessionById(sessionId);

        if (UploadSessionStatus.CANCELED.equals(canceledSession.getStatus()))
            return this;

        events.add(FileUploadSessionCanceledEvent.create(this, canceledSession.cancel()));

        return this;
    }

    private UploadSession fetchUploadSessionById(final UploadSessionID sessionId) {
        return uploadSessions
                .stream()
                .filter(session -> session.getId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> NotFoundException.create(UploadSession.class, sessionId));
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public Long getSize() {
        return size;
    }

    public Set<UploadSession> getUploadSessions() {
        return Set.copyOf(uploadSessions);
    }

    public Queue<Event<?>> getEvents() {
        return new LinkedList<>(events);
    }

}
