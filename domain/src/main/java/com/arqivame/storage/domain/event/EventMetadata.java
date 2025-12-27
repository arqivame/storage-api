package com.arqivame.storage.domain.event;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public record EventMetadata(
        String domain,
        String entity,
        String action,
        String service,
        String version,
        Instant occurredAt,
        Set<EventEntity> relatedEntities) {

    public EventMetadata {
        Objects.requireNonNull(domain);
        Objects.requireNonNull(entity);
        Objects.requireNonNull(action);
        Objects.requireNonNull(service);
        Objects.requireNonNull(version);
        Objects.requireNonNull(occurredAt);

        relatedEntities = Objects.isNull(relatedEntities) ? Set.of() : Set.copyOf(relatedEntities);
    }

    private static final String SERVICE = "com.arqivame.storage.api";
    private static final String DOMAIN = "storage";

    public static EventMetadata create(
            final String entity,
            final String action,
            final String version,
            final Instant occurredAt,
            final Set<EventEntity> relatedEntities) {
        return new EventMetadata(
                DOMAIN,
                entity,
                action,
                SERVICE,
                version,
                occurredAt,
                relatedEntities);
    }

    @Override
    public Set<EventEntity> relatedEntities() {
        return Objects.isNull(relatedEntities) ? Set.of() : Set.copyOf(relatedEntities);
    }

}
