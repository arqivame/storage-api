package com.arqivame.storage.domain.file;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

import com.arqivame.storage.domain.AggregateRoot;
import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventSource;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class File extends AggregateRoot<FileID> implements EventSource {

    private final Checksum checksum;

    private Optional<UploadSession> uploadSession;

    private final Queue<Event<?>> events;

    private File(
            final FileID id,
            final Checksum checksum,
            final Optional<UploadSession> uploadSession,
            final Queue<Event<?>> events) {
        super(id);
        this.checksum = checksum;
        this.uploadSession = uploadSession;

        this.events = Objects.isNull(events) ? new java.util.LinkedList<>() : new java.util.LinkedList<>(events);
    }

    @Override
    public void validate(ValidationHandler handler) {
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    public Optional<Event<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    public File appendChunk(final Chunk chunk) {

        this.uploadSession.ifPresentOrElse((session) -> {
            session.addCompletedChunk(chunk);
        }, () -> {
            throw new RuntimeException("No open upload session to append chunk");
        });

        return this;
    }

    public Boolean hasOpenUploadSession() {

        return uploadSession.filter(session -> !session.isIdleTimeExceeded()).isPresent();
    }

    public UploadSession openUploadSession(final Integer totalChunks, final Duration maxIdleTime) {

        uploadSession.ifPresent((u) -> {
            throw new RuntimeException(
                    "Session already open, please close the current session before opening a new one");
        });

        final UploadSession session = UploadSession.create(totalChunks, maxIdleTime);

        uploadSession = Optional.of(session);

        return session;

    }

    public File closeUploadSession() {

        uploadSession.ifPresentOrElse((u) -> {
            uploadSession = Optional.empty();
        }, () -> {
            throw new RuntimeException("No open upload session to close");
        });

        return this;

    }

    public Checksum getChecksum() {
        return checksum;
    }

}
