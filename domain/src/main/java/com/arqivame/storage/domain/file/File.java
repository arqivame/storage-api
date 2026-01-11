package com.arqivame.storage.domain.file;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

import com.arqivame.storage.domain.AggregateRoot;
import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventSource;
import com.arqivame.storage.domain.exception.InvalidStateException;
import com.arqivame.storage.domain.exception.UploadSessionAlreadyOpenException;
import com.arqivame.storage.domain.exception.DomainException.Error;
import com.arqivame.storage.domain.exception.DownloadSessionAlreadyOpenException;
import com.arqivame.storage.domain.file.event.FileBecameAvailableEvent;
import com.arqivame.storage.domain.file.event.FileCreatedEvent;
import com.arqivame.storage.domain.file.event.FileDownloadSessionOpenedEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionAbortedEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionClosedEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionCompletedEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionOpenedEvent;
import com.arqivame.storage.domain.validation.ValidationError;
import com.arqivame.storage.domain.validation.ValidationHandler;
import com.arqivame.storage.domain.validation.handler.Notification;

public class File extends AggregateRoot<FileID> implements EventSource {

    private final Checksum checksum;
    private final Long size;

    private FileStatus status;

    private Optional<Session> uploadSession;
    private Optional<Session> downloadSession;

    private final Queue<Event<?>> events;

    private File(
            final FileID id,
            final Checksum checksum,
            final Long size,
            final FileStatus status,
            final Optional<Session> uploadSession,
            final Optional<Session> downloadSession,
            final Queue<Event<?>> events) {
        super(id);
        this.checksum = checksum;
        this.size = size;
        this.status = status;
        this.uploadSession = uploadSession;
        this.downloadSession = downloadSession;

        this.events = Objects.isNull(events) ? new LinkedList<>() : new LinkedList<>(events);

        selfValidate();

    }

    public static File with(
            final FileID id,
            final Checksum checksum,
            final Long size,
            final FileStatus status,
            final Optional<Session> uploadSession,
            final Optional<Session> downloadSession,
            final Queue<Event<?>> events) {
        return new File(
                id,
                checksum,
                size,
                status,
                uploadSession,
                downloadSession,
                events);
    }

    public static File create(
            final FileID id,
            final Long size,
            final Checksum checksum) {

        final File file = new File(
                id,
                checksum,
                size,
                FileStatus.NEW,
                Optional.empty(),
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

        if (Objects.isNull(status))
            handler.append(ValidationError.with("File status cannot be null."));

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

        if (FileStatus.UPLOADING.equals(this.status))
            throw InvalidStateException.with(
                    File.class,
                    Error.with("File is already uploading."));

        if (FileStatus.UPLOAD_COMPLETED.equals(this.status))
            throw InvalidStateException.with(
                    File.class,
                    Error.with("Cannot open upload session for a file that has completed upload."));

        if (FileStatus.PROCESSING.equals(this.status))
            throw InvalidStateException.with(
                    File.class,
                    Error.with("Cannot open upload session for a file that is processing."));

        if (FileStatus.AVAILABLE.equals(this.status))
            throw InvalidStateException.with(
                    File.class,
                    Error.with("Cannot open upload session for an available file."));

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
        this.status = FileStatus.UPLOADING;

        events.add(FileUploadSessionOpenedEvent.create(this));

        return this;

    }

    public File openDownloadSession(
            final Integer totalChunks,
            final Long chunkSize,
            final Long lastChunkSize,
            final Long maxBytesPerSecondTransferRatePerChunk,
            final Integer maxChunksAtSameTime) {

        if (!FileStatus.AVAILABLE.equals(this.status))
            throw InvalidStateException.with(
                    File.class,
                    Error.with("Cannot open download session for a file that is not available."));

        final Boolean hasActiveUploadSession = downloadSession.isPresent();

        if (hasActiveUploadSession)
            throw DownloadSessionAlreadyOpenException.create();

        final Session session = Session.create(
                totalChunks,
                chunkSize,
                lastChunkSize,
                maxBytesPerSecondTransferRatePerChunk,
                maxChunksAtSameTime);

        downloadSession = Optional.of(session);

        events.add(FileDownloadSessionOpenedEvent.create(this));

        return this;

    }

    public File completeUploadSession() {

        if (!FileStatus.UPLOADING.equals(this.status))
            throw InvalidStateException.with(File.class, Error.with("File is not in uploading status."));

        if (uploadSession.isEmpty())
            throw InvalidStateException.with(File.class, Error.with("No active upload session to complete."));

        this.status = FileStatus.UPLOAD_COMPLETED;

        events.add(FileUploadSessionCompletedEvent.create(this));

        return this;
    }

    public File abortUploadSession() {

        if (!FileStatus.UPLOADING.equals(this.status))
            throw InvalidStateException.with(File.class, Error.with("File is not in uploading status."));

        if (uploadSession.isEmpty())
            throw InvalidStateException.with(File.class, Error.with("No active upload session to abort."));

        this.status = FileStatus.UPLOAD_ABORTED;

        events.add(FileUploadSessionAbortedEvent.create(this));
        return closeUploadSession();
    }

    public File markAsAvailable() {

        if (FileStatus.AVAILABLE.equals(this.status))
            return this;

        if (!FileStatus.PROCESSING.equals(this.status))
            throw InvalidStateException.with(File.class,
                    Error.with("Only files in processing status can be marked as available."));

        this.status = FileStatus.AVAILABLE;
        events.add(FileBecameAvailableEvent.create(this));
        return closeUploadSession();

    }

    public File markAsFailed() {

        if (FileStatus.FAILED.equals(this.status))
            return this;

        if (FileStatus.AVAILABLE.equals(this.status))
            throw InvalidStateException.with(File.class, Error.with("Cannot mark an available file as failed."));

        this.status = FileStatus.FAILED;
        // events.add(FileBecameFailedEvent.create(this));
        return closeUploadSession();

    }

    private File closeUploadSession() {

        if (uploadSession.isEmpty())
            return this;

        uploadSession = Optional.empty();

        events.add(FileUploadSessionClosedEvent.create(this));

        return this;
    }

    private void selfValidate() {
        final Notification notification = Notification.create();
        validate(notification);
        if (notification.hasErrors())
            throw InvalidStateException.with(File.class, notification.getDomainErrors());
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

    public Optional<Session> getDownloadSession() {
        return downloadSession;
    }

    public Queue<Event<?>> getEvents() {
        return events;
    }

}
