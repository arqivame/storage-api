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

public class FileDownloadSessionOpenedEvent extends Event<FileDownloadSessionOpenedEvent.Data> {

    private static final String ENTITY = "file:download-session";
    private static final String ACTION = "opened";
    private static final String VERSION = "0.0.1";

    private static final FileDownloadSessionOpenedEvent DEFAULT_INSTANCE = new FileDownloadSessionOpenedEvent();

    private FileDownloadSessionOpenedEvent() {
        super(EventMetadata.create(ENTITY, ACTION, VERSION, Instant.now(), Set.of()), null);
    }

    private FileDownloadSessionOpenedEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FileDownloadSessionOpenedEvent.Data data) {
        super(EventMetadata.create(ENTITY, ACTION, VERSION, occurredAt, relatedEntities), data);
    }

    public record Data(UUID fileId) implements Serializable {

        public static Data of(final File file) {
            return new Data(file.getId().getValue());
        }

    }

    public static FileDownloadSessionOpenedEvent create(final File file) {
        return new FileDownloadSessionOpenedEvent(
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
