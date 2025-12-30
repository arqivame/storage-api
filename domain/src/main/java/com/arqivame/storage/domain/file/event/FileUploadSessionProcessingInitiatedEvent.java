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
import com.arqivame.storage.domain.event.EventMetadata;
import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.UploadSession;

public class FileUploadSessionProcessingInitiatedEvent extends Event<FileUploadSessionProcessingInitiatedEvent.Data> {

    private static final String ENTITY = "file:upload-session";
    private static final String ACTION = "processing-initiated";
    private static final String VERSION = "0.0.1";

    private static final FileUploadSessionProcessingInitiatedEvent DEFAULT_INSTANCE = new FileUploadSessionProcessingInitiatedEvent();

    private FileUploadSessionProcessingInitiatedEvent() {
        super(EventMetadata.create(ENTITY, ACTION, VERSION, Instant.now(), Set.of()), null);
    }

    private FileUploadSessionProcessingInitiatedEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FileUploadSessionProcessingInitiatedEvent.Data data) {
        super(EventMetadata.create(ENTITY, ACTION, VERSION, occurredAt, relatedEntities), data);
    }

    public record Data(UUID fileId, UUID sessionId) implements Serializable {

        public static Data of(final File file, final UploadSession session) {
            return new Data(file.getId().getValue(), session.getId().getValue());
        }

    }

    public static FileUploadSessionProcessingInitiatedEvent create(final File file, final UploadSession session) {
        return new FileUploadSessionProcessingInitiatedEvent(
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
