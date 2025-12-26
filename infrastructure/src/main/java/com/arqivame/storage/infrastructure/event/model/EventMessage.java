package com.arqivame.storage.infrastructure.event.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public interface EventMessage<D extends Serializable> extends Serializable {

    public Metadata metadata();

    public D data();

    public record Metadata(
            String domain,
            String entity,
            String action,
            String service,
            String version,
            Instant occurredAt,
            Set<EventEntity> relatedEntities) {
    }

    public record EventEntity(String type, String id) {

    }

}
