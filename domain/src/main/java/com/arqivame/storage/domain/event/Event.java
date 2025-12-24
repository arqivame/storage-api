package com.arqivame.storage.domain.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public abstract class Event<D extends Serializable> implements Serializable {

    private static final String SERVICE = "com.arqivame.storage.api";
    private static final String DOMAIN = "storage";

    private final String domain = DOMAIN;
    private final String entity;
    private final String action;
    private final String service = SERVICE;
    private final String version;
    private final Instant occurredAt;
    private final Set<EventEntity> relatedEntities;
    private final D data;

    public Event(
            final String entity,
            final String action,
            final String version,
            final Instant occurredAt,
            final Set<EventEntity> relatedEntities,
            final D data) {
        this.entity = entity;
        this.action = action;
        this.version = version;
        this.occurredAt = occurredAt;
        this.relatedEntities = Objects.isNull(relatedEntities) ? Set.of() : Set.copyOf(relatedEntities);
        this.data = data;
    }

    public String key() {
        return getDomain() + "." + getEntity() + "." + getAction();
    }

    public String getDomain() {
        return domain;
    }

    public String getEntity() {
        return entity;
    }

    public String getAction() {
        return action;
    }

    public String getService() {
        return service;
    }

    public String getVersion() {
        return version;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Set<EventEntity> getRelatedEntities() {
        return Set.copyOf(relatedEntities);
    }

    public D getData() {
        return data;
    }

}
