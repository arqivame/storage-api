package com.arqivame.storage.domain.file.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventEntity;
import com.arqivame.storage.domain.event.EventMetadata;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSession;

public class FileUploadSessionClosedEvent extends Event<FileUploadSessionClosedEvent.Data> {

    private static final String ENTITY = "file:upload-session";
    private static final String ACTION = "closed";
    private static final String VERSION = "0.0.1";

    private static final FileUploadSessionClosedEvent DEFAULT_INSTANCE = new FileUploadSessionClosedEvent();

    private FileUploadSessionClosedEvent() {
        super(EventMetadata.create(ENTITY, ACTION, VERSION, Instant.now(), Set.of()), null);
    }

    private FileUploadSessionClosedEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FileUploadSessionClosedEvent.Data data) {
        super(EventMetadata.create(ENTITY, ACTION, VERSION, occurredAt, relatedEntities), data);
    }

    public record Data(String fileId, String sessionId, Instant closedAt) implements Serializable {

        public static Data of(final File file, final UploadSession session) {
            return new Data(
                    file.getId().getStringValue(),
                    session.getId().getStringValue(),
                    Instant.now());
        }

    }

    public static FileUploadSessionClosedEvent create(final File file, final UploadSession session) {
        return new FileUploadSessionClosedEvent(
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
