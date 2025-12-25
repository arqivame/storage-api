package com.arqivame.storage.domain.file.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventEntity;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSession;

public class FileUploadSessionCanceledEvent extends Event<FileUploadSessionCanceledEvent.Data> {

    private static final String ENTITY = "file.upload_session";
    private static final String ACTION = "canceled";
    private static final String VERSION = "0.0.1";

    private static final FileUploadSessionCanceledEvent DEFAULT_INSTANCE = new FileUploadSessionCanceledEvent();

    private FileUploadSessionCanceledEvent() {
        super(ENTITY, ACTION, VERSION, null, null, null);
    }

    private FileUploadSessionCanceledEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FileUploadSessionCanceledEvent.Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    public record Data(UUID fileId, UUID sessionId, Instant closedAt) implements Serializable {

        public static Data of(final File file, final UploadSession session) {
            return new Data(
                    file.getId().getValue(),
                    session.getId().getValue(),
                    Instant.now());
        }

    }

    public static FileUploadSessionCanceledEvent create(final File file, final UploadSession session) {
        return new FileUploadSessionCanceledEvent(
                Instant.now(),
                Stream
                        .of(EventEntity.of(file), EventEntity.of(session))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()),
                Data.of(file, session));
    }

    public static String eventKey() {
        return DEFAULT_INSTANCE.key();
    }

}
