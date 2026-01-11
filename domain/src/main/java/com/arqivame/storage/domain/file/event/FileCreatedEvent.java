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

public class FileCreatedEvent extends Event<FileCreatedEvent.Data> {

    private static final String ENTITY = "file";
    private static final String ACTION = "created";
    private static final String VERSION = "0.0.1";

    private static final FileCreatedEvent DEFAULT_INSTANCE = new FileCreatedEvent();

    private FileCreatedEvent() {
        super(EventMetadata.create(ENTITY, ACTION, VERSION, Instant.now(), Set.of()), null);
    }

    private FileCreatedEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FileCreatedEvent.Data data) {
        super(EventMetadata.create(ENTITY, ACTION, VERSION, occurredAt, relatedEntities), data);
    }

    public record Data(UUID fileId) implements Serializable {

        public static Data of(final File file) {
            return new Data(file.getId().getValue());
        }

    }

    public static FileCreatedEvent create(final File file) {
        return new FileCreatedEvent(
                Instant.now(),
                Stream
                        .of(EventEntity.of(file))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()),
                Data.of(file));
    }

    public static String eventKey() {
        return DEFAULT_INSTANCE.key();
    }

}
