package com.arqivame.storage.domain.file;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import com.arqivame.storage.domain.AggregateRoot;
import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventSource;
import com.arqivame.storage.domain.exception.InvalidStateException;
import com.arqivame.storage.domain.exception.UploadSessionAlreadyOpenException;
import com.arqivame.storage.domain.file.event.FileBecameAvailableEvent;
import com.arqivame.storage.domain.file.event.FileChunksClearedEvent;
import com.arqivame.storage.domain.file.event.FileCreatedEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionAbortedEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionCompletedEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionOpenedEvent;
import com.arqivame.storage.domain.file.service.StorageKey;
import com.arqivame.storage.domain.validation.ValidationError;
import com.arqivame.storage.domain.validation.ValidationHandler;
import com.arqivame.storage.domain.validation.handler.Notification;

public class File extends AggregateRoot<FileID> implements EventSource {

    private static final String STORAGE_KEY_PREFIX = "files";

    private final StorageKey storageKey;
    private final Checksum checksum;
    private final Long size;

    private FileStatus status;

    private Optional<Session> uploadSession;
    private final Set<Chunk> uploadedChunks;

    private Optional<Session> downloadSession;

    private final Queue<Event<?>> events;

    private File(
            final FileID id,
            final StorageKey storageKey,
            final Checksum checksum,
            final Long size,
            final FileStatus status,
            final Optional<Session> uploadSession,
            final Set<Chunk> uploadedChunks,
            final Optional<Session> downloadSession,
            final Queue<Event<?>> events) {
        super(id);
        this.storageKey = storageKey;
        this.checksum = checksum;
        this.size = size;
        this.status = status;
        this.uploadSession = uploadSession;
        this.uploadedChunks = Objects.isNull(uploadedChunks) ? new HashSet<>() : new HashSet<>(uploadedChunks);
        this.downloadSession = downloadSession;

        this.events = Objects.isNull(events) ? new LinkedList<>() : new LinkedList<>(events);

        selfValidate();

    }

    public static File with(
            final FileID id,
            final StorageKey storageKey,
            final Checksum checksum,
            final Long size,
            final FileStatus status,
            final Optional<Session> uploadSession,
            final Set<Chunk> uploadedChunks,
            final Optional<Session> downloadSession,
            final Queue<Event<?>> events) {
        return new File(
                id,
                storageKey,
                checksum,
                size,
                status,
                uploadSession,
                uploadedChunks,
                downloadSession,
                events);
    }

    public static File create(
            final FileID id,
            final Long size,
            final Checksum checksum) {

        final File file = new File(
                id,
                StorageKey.create(STORAGE_KEY_PREFIX, id),
                checksum,
                size,
                FileStatus.UPLOADING,
                Optional.empty(),
                Set.of(),
                Optional.empty(),
                new LinkedList<>());

        file.events.add(FileCreatedEvent.create(file));

        return file;
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (Objects.isNull(size))
            handler.append(ValidationError.with("File size cannot be null."));
        else if (size < 0)
            handler.append(ValidationError.with("File size must be a non-negative value."));

    }

    @Override
    public Optional<Event<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    public File openUploadSession(
            final Integer totalChunks,
            final Long chunkSize,
            final Long lastChunkSize,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime) {

        final Boolean hasActiveUploadSession = uploadSession.isPresent();

        if (hasActiveUploadSession)
            throw UploadSessionAlreadyOpenException.create();

        final Session session = Session.create(
                totalChunks,
                chunkSize,
                lastChunkSize,
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime);

        uploadSession = Optional.of(session);

        events.add(FileUploadSessionOpenedEvent.create(this));

        return this;

    }

    public File completeUploadSession() {

        if (uploadSession.isEmpty())
            throw new IllegalStateException("No active upload session to complete.");

        if (uploadedChunks.size() < uploadSession.get().totalChunks())
            throw new IllegalStateException("Cannot complete upload session: not all chunks have been uploaded."); // TODO
                                                                                                                   // exception

        uploadSession = Optional.empty();
        events.add(FileUploadSessionCompletedEvent.create(this));

        return this;
    }

    public File abortUploadSession() {

        if (uploadSession.isEmpty())
            return this;

        uploadSession = Optional.empty();
        // uploadedChunks.clear();
        events.add(FileUploadSessionAbortedEvent.create(this));

        return this;
    }

    public File appendUploadChunk(final Chunk chunk) {

        if (FileStatus.AVAILABLE.equals(this.status))
            throw new IllegalStateException("Cannot append chunk to an available file.");

        this.uploadedChunks.removeIf(c -> c.index().equals(chunk.index()));
        this.uploadedChunks.add(chunk);
        return this;
    }

    public File clearUploadedChunks() {

        if (uploadedChunks.isEmpty())
            return this;

        this.uploadedChunks.clear();

        events.add(FileChunksClearedEvent.create(this));

        return this;

    }

    public File markAsAvailable() {

        if (this.status == FileStatus.AVAILABLE)
            return this;

        this.status = FileStatus.AVAILABLE;
        events.add(FileBecameAvailableEvent.create(this));
        return this;
    }

    private void selfValidate() {
        final Notification notification = Notification.create();
        validate(notification);
        if (notification.hasErrors())
            throw InvalidStateException.with(File.class, notification.getDomainErrors());
    }

    public StorageKey getStorageKey() {
        return storageKey;
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public Long getSize() {
        return size;
    }

    public FileStatus getStatus() {
        return status;
    }

    public Optional<Session> getUploadSession() {
        return uploadSession;
    }

    public Set<Chunk> getUploadedChunks() {
        return Set.copyOf(uploadedChunks);
    }

    public Optional<Session> getDownloadSession() {
        return downloadSession;
    }

    public Queue<Event<?>> getEvents() {
        return events;
    }

}
