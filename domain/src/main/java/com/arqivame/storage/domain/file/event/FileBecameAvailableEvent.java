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

public class FileBecameAvailableEvent extends Event<FileBecameAvailableEvent.Data> {

    private static final String ENTITY = "file";
    private static final String ACTION = "became-available";
    private static final String VERSION = "0.0.1";

    private static final FileBecameAvailableEvent DEFAULT_INSTANCE = new FileBecameAvailableEvent();

    private FileBecameAvailableEvent() {
        super(EventMetadata.create(ENTITY, ACTION, VERSION, Instant.now(), Set.of()), null);
    }

    private FileBecameAvailableEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FileBecameAvailableEvent.Data data) {
        super(EventMetadata.create(ENTITY, ACTION, VERSION, occurredAt, relatedEntities), data);
    }

    public record Data(UUID fileId) implements Serializable {

        public static Data of(final File file) {
            return new Data(file.getId().getValue());
        }

    }

    public static FileBecameAvailableEvent create(final File file) {
        return new FileBecameAvailableEvent(
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
