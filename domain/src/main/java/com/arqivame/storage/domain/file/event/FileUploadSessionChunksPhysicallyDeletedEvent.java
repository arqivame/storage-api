package com.arqivame.storage.domain.file.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventEntity;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSession;

public class FileUploadSessionChunksPhysicallyDeletedEvent
        extends Event<FileUploadSessionChunksPhysicallyDeletedEvent.Data> {

    private static final String ENTITY = "file.upload_session.chunks";
    private static final String ACTION = "physically_deleted";
    private static final String VERSION = "0.0.1";

    private static final FileUploadSessionChunksPhysicallyDeletedEvent DEFAULT_INSTANCE = new FileUploadSessionChunksPhysicallyDeletedEvent();

    private FileUploadSessionChunksPhysicallyDeletedEvent() {
        super(ENTITY, ACTION, VERSION, null, null, null);
    }

    private FileUploadSessionChunksPhysicallyDeletedEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FileUploadSessionChunksPhysicallyDeletedEvent.Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    public record Data(String fileId, String sessionId) implements Serializable {

        public static Data of(final File file, final UploadSession session) {
            return new Data(
                    file.getId().getStringValue(),
                    session.getId().getStringValue());
        }

    }

    public static FileUploadSessionChunksPhysicallyDeletedEvent create(final File file, final UploadSession session) {
        return new FileUploadSessionChunksPhysicallyDeletedEvent(
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
